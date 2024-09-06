package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.telemetry.tracing.Tracing;
import java.util.Locale;
import org.springframework.lang.NonNull;

public sealed interface RequestContext permits DefaultRequestContext {

  @NonNull Tracing tracing();
  @NonNull Locale locale();
  @NonNull Caller originator ();
  @NonNull Caller caller ();
}
