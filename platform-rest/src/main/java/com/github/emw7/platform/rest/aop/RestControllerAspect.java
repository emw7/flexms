package com.github.emw7.platform.rest.aop;

import com.github.emw7.platform.telemetry.tracing.TracingContainer;
import com.github.emw7.platform.telemetry.tracing.TracingFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.lang.NonNull;

@Aspect
public class RestControllerAspect {

  private final TracingFactory tracingFactory;

  public RestControllerAspect(final TracingFactory tracingFactory) {
    this.tracingFactory = tracingFactory;
  }

  @Before(value = "@within(org.springframework.web.bind.annotation.RestController)", argNames = "joinPoint")
  public void tracing(@NonNull final JoinPoint joinPoint) {
    TracingContainer.set(tracingFactory.of());
  }
}
