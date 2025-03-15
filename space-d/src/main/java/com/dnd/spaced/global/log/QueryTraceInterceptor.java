package com.dnd.spaced.global.log;

import com.dnd.spaced.global.consts.LogConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryTraceInterceptor implements HandlerInterceptor {

    private final QueryTraceInspector queryTraceInspector;

    @Override
    public boolean preHandle(
            HttpServletRequest ignoredRequest,
            HttpServletResponse ignoredResponse,
            Object ignoredHandler
    ) throws Exception {
        QueryTracer queryTracer = new QueryTracer();

        queryTraceInspector.set(queryTracer);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        QueryTracer queryTracer = queryTraceInspector.getQueryTracer();
        String requestId = MDC.get(LogConst.REQUEST_ID);
        long queryExecutionTime = queryTracer.calculateExecutionTime(System.currentTimeMillis());
        int sqlCallCount = queryTracer.getSqlCallCount();

        log.info("[{}] execution time : {}ms, sql call count : {}", requestId, queryExecutionTime, sqlCallCount);
    }
}
