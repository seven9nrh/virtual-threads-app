package com.seven9nrh.virtualthreads.application;

import com.seven9nrh.virtualthreads.service.ThreadsService;
import com.seven9nrh.virtualthreads.service.ThreadsService.ThreadResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

@Controller
public class ThreadsController {

  ThreadsService threadService;

  public ThreadsController(ThreadsService threadService) {
    this.threadService = threadService;
  }

  @RequestMapping
  public String init(Model model) {
    return "threads";
  }

  @RequestMapping("/threads")
  public String start(
    Model model,
    @RequestParam(name = "num", required = true) int num
  ) {
    Flux<ThreadResult> flux = threadService.execute(num);
    IReactiveDataDriverContextVariable reactiveDataDriverContextVariable = new ReactiveDataDriverContextVariable(
      flux,
      1
    );
    model.addAttribute("threadResults", reactiveDataDriverContextVariable);
    return "threads";
  }
}
