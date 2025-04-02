package com.dnd.spaced.global.log;

import com.dnd.spaced.global.consts.LogConst;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class RequestIdFilter implements Filter {

    private static final String REQUEST_ID_HEADER_NAME = "X-RequestID";

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        String requestId = ((HttpServletRequest)servletRequest).getHeader(REQUEST_ID_HEADER_NAME);
        MDC.put(LogConst.REQUEST_ID, requestId);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
