package com.github.emw7.platform.service.core.request.context;

import io.micrometer.tracing.Span;
import org.springframework.lang.NonNull;

public interface RequestContextRetriever {

  @NonNull
  Span retrieveTrace(@NonNull final Object context);

  @NonNull
  Caller retrieveOriginator(@NonNull final Object context);

  @NonNull
  Caller retrieveCaller(@NonNull final Object context);

}
