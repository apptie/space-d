package com.dnd.spaced.global.log;

import org.springframework.stereotype.Component;

@Component
public class QueryTracer {

    private final ThreadLocal<Long> startTime = ThreadLocal.withInitial(() -> 0L);
    private final ThreadLocal<Integer> totalQueryCounter = ThreadLocal.withInitial(() -> 0);
    private final ThreadLocal<Integer> crudQueryCounter = ThreadLocal.withInitial(() -> 0);

    public void clear() {
        startTime.remove();
        totalQueryCounter.remove();
        crudQueryCounter.remove();
    }

    public void init() {
        startTime.set(System.currentTimeMillis());
        totalQueryCounter.set(0);
        crudQueryCounter.set(0);
    }

    public void increaseTotalCount() {
        totalQueryCounter.set(totalQueryCounter.get() + 1);
    }

    public void increaseCrudCount() {
        totalQueryCounter.set(totalQueryCounter.get() + 1);
        crudQueryCounter.set(crudQueryCounter.get() + 1);
    }

    public long calculateExecutionTime(long endTime) {
        return endTime - startTime.get();
    }

    public int getTotalQueryCount() {
        return totalQueryCounter.get();
    }

    public int getCrudQueryCount() {
        return crudQueryCounter.get();
    }
}
