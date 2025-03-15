package com.dnd.spaced.global.log;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class QueryTraceInspector implements StatementInspector {

    private final ThreadLocal<QueryTracer> tracer = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        QueryTracer queryTracer = tracer.get();

        if (queryTracer != null) {
            queryTracer.increaseSqlCallCount();
        }
        return sql;
    }

    public void set(QueryTracer queryTracer) {
        this.tracer.set(queryTracer);
    }

    public void clear() {
        this.tracer.remove();
    }

    public QueryTracer getQueryTracer() {
        return this.tracer.get();
    }
}
