package com.github.emw7.platform.service.core.request.context;

import io.micrometer.tracing.Span;
import java.util.Locale;
import org.springframework.lang.NonNull;

public sealed interface RequestContext permits DefaultRequestContext {

  @NonNull
  Span tracing();

  @NonNull
  Locale locale();

  @NonNull
  Caller originator();

  @NonNull
  Caller caller();
}
