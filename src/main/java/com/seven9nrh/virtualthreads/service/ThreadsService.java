package com.seven9nrh.virtualthreads.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ThreadsService {

  private ApplicationContext applicationContext;

  public ThreadsService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public Flux<ThreadResult> execute(int num) {
    ThreadsService service = applicationContext.getBean(ThreadsService.class);
    // stop if num is less than 1
    if (num < 1) {
      return Flux.empty();
    }
    CompletableFuture<ThreadResult>[] futures = new CompletableFuture[num];
    return Flux.<ThreadResult>create(
      sink -> {
        for (int i = 0; i < num; i++) {
          CompletableFuture<ThreadResult> future = service.executeAsync();
          futures[i] = future;
          future.thenAccept(action -> sink.next(action));
        }
        CompletableFuture.allOf(futures).thenRunAsync(() -> sink.complete());
      }
    );
  }

  @Async("asyncThread")
  public CompletableFuture<ThreadResult> executeAsync() {
    LocalDateTime start = LocalDateTime.now();
    String threadName = String.valueOf(Thread.currentThread().threadId());
    try {
      Thread.sleep((long) (Math.floor(Math.random() * 11) * 1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    LocalDateTime end = LocalDateTime.now();
    Duration duration = Duration.between(start, end);
    var threadResult = new ThreadResult(
      start.truncatedTo(ChronoUnit.SECONDS),
      end.truncatedTo(ChronoUnit.SECONDS),
      duration.getSeconds(),
      threadName
    );
    return CompletableFuture.completedFuture(threadResult);
  }

  public static class ThreadResult {

    private LocalDateTime start;
    private LocalDateTime end;
    private long duration;
    private String threadName;

    public ThreadResult(
      LocalDateTime start,
      LocalDateTime end,
      long duration,
      String threadName
    ) {
      this.start = start;
      this.end = end;
      this.duration = duration;
      this.threadName = threadName;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public LocalDateTime getEnd() {
      return end;
    }

    public long getDuration() {
      return duration;
    }

    public String getThreadName() {
      return threadName;
    }
  }
}
