package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.telemetry.tracing.Trace;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class DefaultRequestContextRetriever implements RequestContextRetriever {

  //region API
  @Override
  public final @NonNull Trace retrieveTrace(@NonNull final Object context) {
    final Trace trace = _retrieveTrace(context);
    if (trace != null) {
      return trace;
    } else {
      return new Trace();
    }
  }

  @Override
  public final @NonNull Caller retrieveOriginator(@NonNull final Object context) {
    final Caller originator = _retrieveOriginator(context);
    if (originator != null) {
      return originator;
    } else {
      return Caller.DEFAULT;
    }
  }

  @Override
  public final @NonNull Caller retrieveCaller(@NonNull final Object context) {
    final Caller caller = _retrieveCaller(context);
    if (caller != null) {
      return caller;
    } else {
      return Caller.DEFAULT;
    }
  }
  //endregion API

  //region Template methods
  protected @Nullable Trace _retrieveTrace(@NonNull final Object context) {
    return null;
  }

  protected @Nullable Caller _retrieveOriginator(@NonNull final Object context) {
    return null;
  }

  protected @Nullable Caller _retrieveCaller(@NonNull final Object context) {
    return null;
  }
  //endregion Template methods

}
