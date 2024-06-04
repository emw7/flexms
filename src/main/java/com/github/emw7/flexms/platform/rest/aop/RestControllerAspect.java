package com.github.emw7.flexms.platform.rest.aop;

import com.github.emw7.flexms.platform.telemetry.Tracing;
import com.github.emw7.flexms.platform.telemetry.TracingContainer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestControllerAspect {

  private final Tracing tracing;

  private RestControllerAspect(final Tracing tracing) {
    this.tracing = tracing;
  }

  @Before(value = "@within(org.springframework.web.bind.annotation.RestController)", argNames = "joinPoint")
  public void tracing (@NonNull final JoinPoint joinPoint)
  {
    TracingContainer.set(tracing);
  }
}
