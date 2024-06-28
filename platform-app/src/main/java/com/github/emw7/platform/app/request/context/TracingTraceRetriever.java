package com.github.emw7.platform.app.request.context;

import com.github.emw7.platform.telemetry.tracing.Trace;
import org.springframework.lang.Nullable;

public interface TracingTraceRetriever {

  @Nullable
  Trace retrieve();

}
