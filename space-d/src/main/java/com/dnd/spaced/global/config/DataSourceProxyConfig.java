package com.dnd.spaced.global.config;

import com.dnd.spaced.global.consts.LogConst;
import com.dnd.spaced.global.log.QueryTracer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class DataSourceProxyConfig {

    private static final String SET_OPTION_METHOD = "setAutoCommit|commit|rollback|setReadOnly|setTransactionIsolation|setHoldability|setCatalog|setSchema";

    private final QueryTracer queryTracer;

    @Bean
    public BeanPostProcessor dataSourcePostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DataSource dataSourceBean && !(Proxy.isProxyClass(bean.getClass()))) {
                    return Proxy.newProxyInstance(
                            dataSourceBean.getClass().getClassLoader(),
                            findInterfaces(dataSourceBean),
                            getConnectionInvocationHandler(dataSourceBean)
                    );
                }
                return bean;
            }

            private InvocationHandler getConnectionInvocationHandler(DataSource dataSourceBean) {
                SQLExceptionTranslator exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSourceBean);

                return (proxy, method, args) -> {
                    try {
                        Object result = method.invoke(dataSourceBean, args);

                        if ("getConnection".equals(method.getName()) && result instanceof Connection) {
                            Connection connection = (Connection) result;

                            return Proxy.newProxyInstance(
                                    connection.getClass().getClassLoader(),
                                    findInterfaces(connection),
                                    getStatementInvocationHandler(connection, exceptionTranslator)
                            );
                        }
                        return result;
                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        if (cause instanceof SQLException) {
                            DataAccessException dae = exceptionTranslator.translate("DataSource operation", null, (SQLException) cause);
                            throw dae != null ? dae : new UncategorizedSQLException("DataSource operation failed", null, (SQLException) cause);
                        }
                        throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
                    }
                };
            }

            private InvocationHandler getStatementInvocationHandler(Connection connection, SQLExceptionTranslator exceptionTranslator) {
                String requestId = MDC.get(LogConst.REQUEST_ID);

                return (connProxy, connMethod, connArgs) -> {
                    try {
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
                            return createStatementProxy(statement, "", exceptionTranslator);
                        }
                        if ("prepareStatement".equals(connMethod.getName())
                                && connResult instanceof PreparedStatement preparedStatement
                                && connArgs != null && connArgs[0] instanceof String sql
                        ) {
                            return createPreparedStatementProxy(preparedStatement, (String) connArgs[0], exceptionTranslator);
                        }
                        if ("prepareCall".equals(connMethod.getName())
                                && connResult instanceof CallableStatement callableStatement
                                && connArgs != null && connArgs[0] instanceof String sql
                        ) {
                            return createCallableStatementProxy(callableStatement, (String) connArgs[0], exceptionTranslator);
                        }

                        return connResult;
                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        if (cause instanceof SQLException) {
                            DataAccessException dae = exceptionTranslator.translate("Connection operation", null, (SQLException) cause);
                            throw dae != null ? dae : new UncategorizedSQLException("Connection operation failed", null, (SQLException) cause);
                        }
                        throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
                    }
                };
            }
        };
    }

    private boolean isTransactionControlMethod(String methodName) {
        return methodName.matches(SET_OPTION_METHOD);
    }

    private Statement createStatementProxy(Statement stmt, String sql, SQLExceptionTranslator exceptionTranslator) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (Statement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                findInterfaces(stmt),
                (proxy, method, args) -> {
                    try {
                        if (method.getName().startsWith("execute")) {
                            queryTracer.increaseCrudCount();
                            String executeSql = sql;
                            if (args != null && args.length > 0 && args[0] instanceof String argSql) {
                                executeSql = argSql;
                            }
                            log.info("[{}] {}", requestId, executeSql);
                        }
                        return method.invoke(stmt, args);
                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        if (cause instanceof SQLException) {
                            String querySql = sql;
                            if (args != null && args.length > 0 && args[0] instanceof String argSql) {
                                querySql = argSql;
                            }
                            DataAccessException dae = exceptionTranslator.translate("Statement execution", querySql, (SQLException) cause);
                            throw dae != null ? dae : new UncategorizedSQLException("Statement execution failed", querySql, (SQLException) cause);
                        }
                        throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
                    }
                }
        );
    }

    private PreparedStatement createPreparedStatementProxy(PreparedStatement stmt, String sql, SQLExceptionTranslator exceptionTranslator) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (PreparedStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                findInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId, exceptionTranslator)
        );
    }

    private CallableStatement createCallableStatementProxy(CallableStatement stmt, String sql, SQLExceptionTranslator exceptionTranslator) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (CallableStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                findInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId, exceptionTranslator)
        );
    }

    private InvocationHandler getExecuteStatementInvocationHandler(Statement stmt, String sql, String requestId, SQLExceptionTranslator exceptionTranslator) {
        return (proxy, method, args) -> {
            try {
                if (method.getName().startsWith("execute")) {
                    queryTracer.increaseCrudCount();
                    log.info("[{}] {}", requestId, sql);
                }
                return method.invoke(stmt, args);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof SQLException) {
                    DataAccessException dae = exceptionTranslator.translate("Statement execution", sql, (SQLException) cause);
                    throw dae != null ? dae : new UncategorizedSQLException("Statement execution failed", sql, (SQLException) cause);
                }
                throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
            }
        };
    }

    private Class<?>[] findInterfaces(Object obj) {
        Set<Class<?>> interfaces = new HashSet<>();
        Class<?> current = obj.getClass();

        while (current != null) {
            interfaces.addAll(Arrays.asList(current.getInterfaces()));
            current = current.getSuperclass();
        }

        return interfaces.toArray(new Class<?>[0]);
    }
}
