package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.telemetry.tracing.Trace;
import org.springframework.lang.NonNull;

public interface RequestContextRetriever {

  @NonNull Trace retrieveTrace (@NonNull final Object context);

  @NonNull Caller retrieveOriginator (@NonNull final Object context);

  @NonNull Caller retrieveCaller (@NonNull final Object context);

}
