package com.seven9nrh.virtualthreads;

import java.util.concurrent.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class VirtualThreadsApplication {

  public static void main(String[] args) {
    SpringApplication.run(VirtualThreadsApplication.class, args);
  }

  @Bean("asyncThread")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1000);
    executor.setMaxPoolSize(Integer.MAX_VALUE);
    executor.setQueueCapacity(9999);
    executor.setThreadFactory(Thread.ofVirtual().factory()); // <---- Enable Virtual Threads
    return executor;
  }
}
