package com.dnd.spaced.config.clean.annotation;

import com.dnd.spaced.config.clean.CleanUpRedisListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.test.context.TestExecutionListeners;

@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {CleanUpRedisListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface CleanUpRedis {
}
