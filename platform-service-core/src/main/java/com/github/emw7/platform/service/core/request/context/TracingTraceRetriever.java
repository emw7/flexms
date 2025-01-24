package com.github.emw7.platform.service.core.request.context;

import io.micrometer.tracing.Span;
import org.springframework.lang.NonNull;

public interface TracingTraceRetriever {

  @NonNull
  Span retrieve();

}
