package com.dnd.spaced.global.log.proxy;

import com.dnd.spaced.global.log.proxy.util.DataSourceLogProxyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataSourceLogProxyPostProcessor implements BeanPostProcessor {

    private final ObjectProvider<DataSourceLogProxyUtils> utilsProvider;
    private final ObjectProvider<DataSourceLogProxyHandlerFactory> factoryProvider;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource dataSourceBean && !(Proxy.isProxyClass(bean.getClass()))) {
            DataSourceLogProxyUtils utils = utilsProvider.getObject();
            DataSourceLogProxyHandlerFactory factory = factoryProvider.getObject();

            return Proxy.newProxyInstance(
                    dataSourceBean.getClass().getClassLoader(),
                    utils.findInterfaces(dataSourceBean),
                    factory.getConnectionInvocationHandler(dataSourceBean)
            );
        }

        return bean;
    }
}
