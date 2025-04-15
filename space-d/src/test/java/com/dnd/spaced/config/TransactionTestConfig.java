package com.dnd.spaced.config;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Profile("test")
@Configuration
public class TransactionTestConfig {

    @SpyBean
    PlatformTransactionManager platformTransactionManager;
}
