package com.github.emw7.flexms.platform.telemetry;

import org.springframework.lang.NonNull;

public class TracingContainer {

  private static final ThreadLocal<Tracing> tracingContainer= new ThreadLocal<>();

  public static void set (@NonNull final Tracing tracing) {
    tracingContainer.set(tracing);
  }


  public static Tracing get () {
    return tracingContainer.get();
  }
}
