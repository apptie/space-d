package com.dnd.spaced.config.context;

import com.dnd.spaced.config.context.exception.TestContextClassNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.web.bind.annotation.RestController;

public class RestControllerTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext;

        registerConfigurationBean(applicationContext);
        registerControllerSliceTestBean(registry);
    }

    private void registerConfigurationBean(ConfigurableApplicationContext applicationContext) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        beanFactory.registerSingleton("objectMapper", objectMapper());
        beanFactory.registerSingleton("provider", new ManualRestDocumentation());
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.setDateFormat(dateFormat);

        return objectMapper;
    }

    private void registerControllerSliceTestBean(BeanDefinitionRegistry registry) {
        Set<Class<?>> controllerClasses = findRestControllerClasses();
        Map<Class<?>, Object> mockServices = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            Map<String, Class<?>> dependencies = analyzeControllerDependencies(controllerClass);

            processMockServiceBean(registry, dependencies, mockServices);
            registerControllerBean(registry, controllerClass, dependencies);
        }
    }

    private void processMockServiceBean(
            BeanDefinitionRegistry registry,
            Map<String, Class<?>> dependencies,
            Map<Class<?>, Object> mockServices
    ) {
        for (Map.Entry<String, Class<?>> entry : dependencies.entrySet()) {
            Class<?> serviceClass = entry.getValue();
            Object mockService = mockServices.computeIfAbsent(serviceClass, Mockito::mock);
            String serviceBeanName = getBeanName(serviceClass);

            registerMockServiceBean(registry, serviceBeanName, serviceClass, mockService);
        }
    }

    private Set<Class<?>> findRestControllerClasses() {
        Set<Class<?>> results = new HashSet<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        final String basePackage = "com.dnd.spaced";

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)) {
            try {
                results.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new TestContextClassNotFoundException(e);
            }
        }

        return results;
    }

    private Map<String, Class<?>> analyzeControllerDependencies(Class<?> controllerClass) {
        Map<String, Class<?>> dependencies = new HashMap<>();
        Constructor<?> constructor = controllerClass.getDeclaredConstructors()[0];

        for (Parameter param : constructor.getParameters()) {
            Class<?> paramType = param.getType();

            dependencies.put(param.getName(), paramType);
        }

        return dependencies;
    }

    private void registerMockServiceBean(
            BeanDefinitionRegistry registry,
            String beanName,
            Class<?> serviceClass,
            Object mockService
    ) {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(MockServiceFactoryBean.class);

        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addGenericArgumentValue(mockService);
        args.addGenericArgumentValue(serviceClass);
        definition.setConstructorArgumentValues(args);

        registry.registerBeanDefinition(beanName, definition);
    }

    private void registerControllerBean(
            BeanDefinitionRegistry registry,
            Class<?> controllerClass,
            Map<String, Class<?>> dependencies
    ) {
        String controllerBeanName = getBeanName(controllerClass);

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(controllerClass);

        Constructor<?> constructor = controllerClass.getDeclaredConstructors()[0];
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        Parameter[] parameters = constructor.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();

            if (dependencies.containsKey(param.getName())) {
                args.addIndexedArgumentValue(i, new RuntimeBeanReference(getBeanName(paramType)));
            }

            definition.setConstructorArgumentValues(args);
        }

        registry.registerBeanDefinition(controllerBeanName, definition);
    }

    private String getBeanName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();

        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    public static class MockServiceFactoryBean implements FactoryBean<Object> {

        private final Object mockInstance;
        private final Class<?> serviceType;

        public MockServiceFactoryBean(Object mockInstance, Class<?> serviceType) {
            this.mockInstance = mockInstance;
            this.serviceType = serviceType;
        }

        @Override
        public Object getObject() {
            return mockInstance;
        }

        @Override
        public Class<?> getObjectType() {
            return serviceType;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }
}
