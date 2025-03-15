package com.dnd.spaced.global.log;

import lombok.Getter;

@Getter
public class QueryTracer {

    private int sqlCallCount = 0;
    private final long startTime;

    public QueryTracer() {
        this.startTime = System.currentTimeMillis();
    }

    public long calculateExecutionTime(long endTime) {
        return endTime - startTime;
    }

    public void increaseSqlCallCount() {
        sqlCallCount++;
    }
}
