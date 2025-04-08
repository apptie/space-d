package com.dnd.spaced.global.log.proxy;

import com.dnd.spaced.global.consts.LogConst;
import com.dnd.spaced.global.log.QueryTracer;
import com.dnd.spaced.global.log.proxy.util.DataSourceLogProxyUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceLogProxyHandlerFactory {

    private static final String SET_OPTION_METHOD = "setAutoCommit|commit|rollback|setReadOnly|setTransactionIsolation|setHoldability|setCatalog|setSchema";

    private final QueryTracer queryTracer;
    private final DataSourceLogProxyUtils dataSourceLogProxyUtils;
    private final ObjectProvider<SQLExceptionTranslator> exceptionTranslatorProvider;

    public InvocationHandler getConnectionInvocationHandler(DataSource dataSourceBean) {
        return (proxy, method, args) -> {
            try {
                Object result = method.invoke(dataSourceBean, args);

                return createProxyConnection(method, result);
            } catch (InvocationTargetException ex) {
                throw handleInvocationTargetException(ex, "DataSource operation", null);
            }
        };
    }

    private Object createProxyConnection(Method method, Object result) {
        if ("getConnection".equals(method.getName()) && result instanceof Connection connection) {
            return Proxy.newProxyInstance(
                    connection.getClass().getClassLoader(),
                    dataSourceLogProxyUtils.findInterfaces(connection),
                    getStatementInvocationHandler(connection)
            );
        }

        return result;
    }

    private Statement createStatementProxy(Statement stmt) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (Statement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                dataSourceLogProxyUtils.findInterfaces(stmt),
                (proxy, method, args) -> {
                    String targetSql = "";

                    try {
                        if (isExecuteMethod(method)) {
                            queryTracer.increaseCrudCount();
                            targetSql = findSql(args, targetSql);
                            log.info("[{}] {}", requestId, targetSql);
                        }

                        return method.invoke(stmt, args);
                    } catch (InvocationTargetException ex) {
                        targetSql = findSql(args, targetSql);

                        throw handleInvocationTargetException(ex, "Statement execution", targetSql);
                    }
                }
        );
    }

    private String findSql(Object[] args, String targetSql) {
        if (args != null && args.length > 0 && args[0] instanceof String argSql) {
            targetSql = argSql;
        }
        return targetSql;
    }

    private InvocationHandler getStatementInvocationHandler(Connection connection) {
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

                return getProxyStatementInvocationHandler(connMethod, connArgs, connResult);
            } catch (InvocationTargetException ex) {
                throw handleInvocationTargetException(ex, "Connection operation", null);
            }
        };
    }

    private Object getProxyStatementInvocationHandler(Method connMethod, Object[] connArgs, Object connResult) {
        if ("createStatement".equals(connMethod.getName()) && connResult instanceof Statement statement) {
            return createStatementProxy(statement);
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
    }

    private boolean isTransactionControlMethod(String methodName) {
        return methodName.matches(SET_OPTION_METHOD);
    }

    private PreparedStatement createPreparedStatementProxy(PreparedStatement stmt, String sql) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (PreparedStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                dataSourceLogProxyUtils.findInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId)
        );
    }

    private CallableStatement createCallableStatementProxy(CallableStatement stmt, String sql) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        return (CallableStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                dataSourceLogProxyUtils.findInterfaces(stmt),
                getExecuteStatementInvocationHandler(stmt, sql, requestId)
        );
    }

    private InvocationHandler getExecuteStatementInvocationHandler(Statement stmt, String sql, String requestId) {
        return (proxy, method, args) -> {
            try {
                if (isExecuteMethod(method)) {
                    queryTracer.increaseCrudCount();
                    log.info("[{}] {}", requestId, sql);
                }

                return method.invoke(stmt, args);
            } catch (InvocationTargetException ex) {
                throw handleInvocationTargetException(ex, "Statement execution", sql);
            }
        };
    }

    private boolean isExecuteMethod(Method method) {
        return method.getName().startsWith("execute");
    }

    private RuntimeException handleInvocationTargetException(
            InvocationTargetException ex,
            String task,
            String sql
    ) {
        Throwable cause = ex.getCause();

        if (cause instanceof SQLException targetCause) {
            DataAccessException dae = exceptionTranslatorProvider.getObject()
                                                                 .translate(task, sql, targetCause);

            return dae != null ? dae : new UncategorizedSQLException(task + " failed", sql, targetCause);
        }

        return cause instanceof RuntimeException targetCause ? targetCause : new RuntimeException(cause);
    }
}
