package com.dnd.spaced.global.log;

import com.dnd.spaced.global.consts.LogConst;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

public class MDCTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                if (contextMap != null) {
                    String originalRequestId = contextMap.get(LogConst.REQUEST_ID);
                    if (originalRequestId != null) {
                        contextMap.put(LogConst.REQUEST_ID, originalRequestId + "-async");
                    }
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
