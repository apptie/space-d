package com.dnd.spaced.global.config;

import com.dnd.spaced.global.consts.LogConst;
import com.dnd.spaced.global.log.QueryTracer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceProxyConfig {

    private static final String TRANSACTION_METHOD_NAME = "setAutoCommit|commit|rollback|setReadOnly|setTransactionIsolation|setHoldability|setCatalog|setSchema";

    private final QueryTracer queryTracer;

    @Bean
    public BeanPostProcessor dataSourcePostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DataSource dataSourceBean && !(Proxy.isProxyClass(bean.getClass()))) {
                    return Proxy.newProxyInstance(
                            dataSourceBean.getClass().getClassLoader(),
                            new Class<?>[] { DataSource.class },
                            getConnectionInvocationHandler(dataSourceBean)
                    );
                }
                return bean;
            }

            private InvocationHandler getConnectionInvocationHandler(DataSource dataSourceBean) {
                return (proxy, method, args) -> {
                    Object result = method.invoke(dataSourceBean, args);

                    if ("getConnection".equals(method.getName()) && result instanceof Connection) {
                        Connection connection = (Connection) result;

                        return Proxy.newProxyInstance(
                                connection.getClass().getClassLoader(),
                                new Class<?>[]{Connection.class},
                                getStatementInvocationHandler(connection)
                        );
                    }
                    return result;
                };
            }

            private InvocationHandler getStatementInvocationHandler(Connection connection) {
                String requestId = MDC.get(LogConst.REQUEST_ID);

                return (connProxy, connMethod, connArgs) -> {
                    if (isTransactionControlMethod(connMethod.getName())) {
                        queryTracer.increaseTotalCount();
                        log.info(
                                "[{}] {} {}",
                                requestId,
                                connMethod.getName(),
                                (connArgs != null && connArgs.length > 0 ? connArgs[0] : "")
                        );
                    }

                    Object connResult = connMethod.invoke(connection, connArgs);

                    if ("createStatement".equals(connMethod.getName()) && connResult instanceof Statement statement) {
                        return createStatementProxy(statement, "");
                    }
                    if ("prepareStatement".equals(connMethod.getName())
                            && connResult instanceof PreparedStatement preparedStatement
                            && connArgs != null && connArgs[0] instanceof String sql
                    ) {
                        return createPreparedStatementProxy(preparedStatement, sql);
                    }
                    if ("prepareCall".equals(connMethod.getName())
                            && connResult instanceof CallableStatement callableStatement
                            && connArgs != null && connArgs[0] instanceof String sql
                    ) {
                        return createCallableStatementProxy(callableStatement, sql);
                    }

                    return connResult;
                };
            }
        };
    }

    private boolean isTransactionControlMethod(String methodName) {
        return methodName.matches(TRANSACTION_METHOD_NAME);
    }

    private Statement createStatementProxy(Statement stmt, String sql) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (Statement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                getInterfaces(stmt),
                (proxy, method, args) -> {
                    if (method.getName().startsWith("execute")) {
                        queryTracer.increaseCrudCount();
                        String executeSql = sql;
                        if (args != null && args.length > 0 && args[0] instanceof String argSql) {
                            executeSql = argSql;
                        }
                        log.info("[{}] {}", requestId, executeSql);
                    }
                    return method.invoke(stmt, args);
                }
        );
    }

    private PreparedStatement createPreparedStatementProxy(PreparedStatement stmt, String sql) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (PreparedStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                getInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId)
        );
    }

    private CallableStatement createCallableStatementProxy(CallableStatement stmt, String sql) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (CallableStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                getInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId)
        );
    }

    private InvocationHandler getExecuteStatementInvocationHandler(Statement stmt, String sql, String requestId) {
        return (proxy, method, args) -> {
            if (method.getName().startsWith("execute")) {
                queryTracer.increaseCrudCount();
                log.info("[{}] {}", requestId, sql);
            }
            return method.invoke(stmt, args);
        };
    }

    private Class<?>[] getInterfaces(Object obj) {
        Set<Class<?>> interfaces = new HashSet<>();
        Class<?> current = obj.getClass();

        while (current != null) {
            interfaces.addAll(Arrays.asList(current.getInterfaces()));
            current = current.getSuperclass();
        }

        return interfaces.toArray(new Class<?>[0]);
    }
}
