package com.github.emw7.platform.telemetry.tracing;

import org.springframework.lang.NonNull;

public interface TracingFactory {

  @NonNull
  Tracing of();

  @NonNull
  Tracing of(@NonNull final String traceId);

}
