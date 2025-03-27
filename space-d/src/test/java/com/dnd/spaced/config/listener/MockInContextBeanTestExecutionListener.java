package com.dnd.spaced.config.listener;

import com.dnd.spaced.config.annotation.MockInContextBean;
import com.dnd.spaced.config.annotation.SpyInContextBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mockito.Mockito;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.ReflectionUtils;

public class MockInContextBeanTestExecutionListener extends AbstractTestExecutionListener {

    private final Map<Object, List<FieldReplacement>> replacementsMap = new ConcurrentHashMap<>();

    private static class FieldReplacement {

        private final Object targetBean;
        private final Field targetField;
        private final Object originalValue;

        public FieldReplacement(Object targetBean, Field targetField, Object originalValue) {
            this.targetBean = targetBean;
            this.targetField = targetField;
            this.originalValue = originalValue;
        }

        public void restore() throws IllegalAccessException {
            ReflectionUtils.makeAccessible(targetField);

            targetField.set(targetBean, originalValue);
        }
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        Object testInstance = testContext.getTestInstance();
        ApplicationContext appContext = testContext.getApplicationContext();
        List<FieldReplacement> replacements = new ArrayList<>();

        ReflectionUtils.doWithFields(testInstance.getClass(), field -> {
            try {
                processMockBeanInContextBean(testInstance, field, appContext, replacements);
                processSpyBeanInContextBean(testInstance, field, appContext, replacements);
            } catch (Exception e) {
                throw new RuntimeException("필드 교체 실패 : " + field.getName(), e);
            }
        });

        replacementsMap.put(testInstance, replacements);
    }

    private void processMockBeanInContextBean(
            Object testInstance,
            Field field,
            ApplicationContext applicationContext,
            List<FieldReplacement> replacements
    ) throws Exception {
        MockInContextBean annotation = field.getAnnotation(MockInContextBean.class);

        if (annotation == null) {
            return;
        }

        Object mockObject = Mockito.mock(field.getType());
        applyMockBeanToTargetBeans(field, applicationContext, replacements, annotation, mockObject);

        field.setAccessible(true);
        field.set(testInstance, mockObject);
    }

    private void applyMockBeanToTargetBeans(
            Field field,
            ApplicationContext applicationContext,
            List<FieldReplacement> replacements,
            MockInContextBean annotation,
            Object mockObject
    ) throws Exception {
        Class<?>[] targetBeanClasses = annotation.value();
        String targetBeanName = annotation.name().isEmpty() ? null : annotation.name();

        for (Class<?> targetBeanClass : targetBeanClasses) {
            Object targetBean = getTargetBean(applicationContext, targetBeanClass, targetBeanName);
            Field targetField = findTargetField(targetBeanClass, field.getName());
            ReflectionUtils.makeAccessible(targetField);
            Object originalValue = targetField.get(targetBean);

            setFieldValue(targetBean, targetField, mockObject);
            replacements.add(new FieldReplacement(targetBean, targetField, originalValue));
        }
    }

    private void processSpyBeanInContextBean(
            Object testInstance,
            Field field,
            ApplicationContext applicationContext,
            List<FieldReplacement> replacements
    ) throws Exception {
        SpyInContextBean annotation = field.getAnnotation(SpyInContextBean.class);

        if (annotation == null) {
            return;
        }

        Object spyObject = Mockito.spy(field.getType());
        applySpyBeanToTargetBeans(field, applicationContext, replacements, annotation, spyObject);

        field.setAccessible(true);
        field.set(testInstance, spyObject);
    }

    private void applySpyBeanToTargetBeans(
            Field field,
            ApplicationContext applicationContext,
            List<FieldReplacement> replacements,
            SpyInContextBean annotation,
            Object spyObject
    ) throws Exception {
        Class<?>[] targetBeanClasses = annotation.value();
        String targetBeanName = annotation.name().isEmpty() ? null : annotation.name();

        for (Class<?> targetBeanClass : targetBeanClasses) {
            Object targetBean = getTargetBean(applicationContext, targetBeanClass, targetBeanName);
            Field targetField = findTargetField(targetBeanClass, field.getName());
            ReflectionUtils.makeAccessible(targetField);
            Object originalValue = targetField.get(targetBean);

            setFieldValue(targetBean, targetField, spyObject);
            replacements.add(new FieldReplacement(targetBean, targetField, originalValue));
        }
    }

    private Object getTargetBean(
            ApplicationContext applicationContext,
            Class<?> targetBeanClass,
            String targetBeanName
    ) {
        Object targetBean = targetBeanName == null ?
                applicationContext.getBean(targetBeanClass) :
                applicationContext.getBean(targetBeanName, targetBeanClass);

        if (AopUtils.isAopProxy(targetBean)) {
            return AopProxyUtils.getSingletonTarget(targetBean);
        }

        return targetBean;
    }

    private Field findTargetField(Class<?> targetBeanClass, String fieldName) {
        Field targetField = ReflectionUtils.findField(targetBeanClass, fieldName);

        if (targetField == null) {
            throw new IllegalStateException("지정한 클래스 " + targetBeanClass.getName() + " 에서 " + fieldName + " 필드를 찾을 수 없습니다.");
        }

        return targetField;
    }

    private void setFieldValue(Object targetBean, Field targetField, Object newValue) throws Exception {
        if (Modifier.isFinal(targetField.getModifiers())) {
            setFinalFieldWithUnsafe(targetBean, targetField, newValue);
            return;
        }

        targetField.set(targetBean, newValue);
    }

    private void setFinalFieldWithUnsafe(Object targetBean, Field targetField, Object newValue) throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Object unsafe = unsafeField.get(null);

        Method objectFieldOffsetMethod = unsafeClass.getMethod("objectFieldOffset", Field.class);
        long offset = (long) objectFieldOffsetMethod.invoke(unsafe, targetField);

        Method putObjectMethod = unsafeClass.getMethod("putObject", Object.class, long.class, Object.class);
        putObjectMethod.invoke(unsafe, targetBean, offset, newValue);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws IllegalAccessException {
        Object testInstance = testContext.getTestInstance();
        List<FieldReplacement> replacements = replacementsMap.remove(testInstance);
        
        if (replacements == null || replacements.isEmpty()) {
            return;
        }
        
        for (FieldReplacement replacement : replacements) {
            replacement.restore();
        }
    }
}
