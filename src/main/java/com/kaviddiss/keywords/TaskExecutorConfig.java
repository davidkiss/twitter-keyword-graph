package com.kaviddiss.keywords;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by david on 2014-12-26.
 */
@Configuration
public class TaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(final @Value("${taskExecutor.corePoolSize}") int corePoolSize,
                                               final @Value("${taskExecutor.maxPoolSize}") int maxPoolSize,
                                               final @Value("${taskExecutor.queueCapacity}") int queueCapacity) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        return taskExecutor;
    }
}
