package app.Configs.Threads;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPool {

    @Bean(name = "taskExecutor")
    public Executor threadPoolExecutor(){
        var threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(10);
        threadPool.setMaxPoolSize(20);
        threadPool.setQueueCapacity(50);
        threadPool.setThreadNamePrefix("async-threadpool-");
        threadPool.initialize();
        return  threadPool;
    }
}
