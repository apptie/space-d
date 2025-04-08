package com.dnd.spaced.global.config;

import com.dnd.spaced.global.log.proxy.DataSourceLogProxyPostProcessor;
import com.dnd.spaced.global.log.proxy.DataSourceLogProxyHandlerFactory;
import com.dnd.spaced.global.log.proxy.util.DataSourceLogProxyUtils;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class DataSourceLogProxyConfig {

    @Bean
    public static BeanPostProcessor dataSourceLogProxyPostProcessor(
            ObjectProvider<DataSourceLogProxyUtils> utilsProvider,
            ObjectProvider<DataSourceLogProxyHandlerFactory> factoryProvider
    ) {
        return new DataSourceLogProxyPostProcessor(utilsProvider, factoryProvider);
    }

    @Bean
    public SQLExceptionTranslator sqlExceptionTranslator(DataSource dataSource) {
        return new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }
}
