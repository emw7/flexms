package com.github.emw7.platform.app.rest.request.context;

import com.github.emw7.platform.app.request.context.Caller;
import com.github.emw7.platform.app.request.context.CallerRetriever;
import com.github.emw7.platform.app.request.context.DefaultRequestContextRetriever;
import com.github.emw7.platform.app.request.context.OriginatorRetriever;
import com.github.emw7.platform.app.request.context.TracingTraceRetriever;
import com.github.emw7.platform.telemetry.tracing.Trace;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class DefaultHttpServletHeadersRequestContextRetriever extends
    DefaultRequestContextRetriever implements RestRequestContextRetriever {

  //region Private properties
  private final RestTracingTraceRetriever tracingTraceRetriever;
  private final RestOriginatorRetriever originatorRetriever;
  private final RestCallerRetriever callerRetriever;
  //endregion Provate properties

  //region Constructors
  public DefaultHttpServletHeadersRequestContextRetriever(
      @Nullable final RestTracingTraceRetriever tracingTraceRetriever,
      @Nullable final RestOriginatorRetriever originatorRetriever,
      @Nullable final RestCallerRetriever callerRetriever) {
    this.tracingTraceRetriever = tracingTraceRetriever;
    this.originatorRetriever = originatorRetriever;
    this.callerRetriever = callerRetriever;
  }
  //endregion Constructors

  //region Template methods
  @Override
  protected final @Nullable Trace _retrieveTrace(@NonNull final Object context) {
    if (getTracingTraceRetriever() != null) {
      return getTracingTraceRetriever().retrieve();
    } else {
      return null;
    }
  }


  @Override
  protected final @Nullable Caller _retrieveOriginator(@NonNull final Object context) {
    if (getOriginatorRetriever() != null) {
      return getOriginatorRetriever().retrieve(context);
    } else {
      return null;
    }
  }


  @Override
  protected final @Nullable Caller _retrieveCaller(@NonNull final Object context) {
    if (getCallerRetriever() != null) {
      return getCallerRetriever().retrieve(context);
    } else {
      return null;
    }
  }
  //endregion Template methods

  //region Getters & Setters
  private @Nullable TracingTraceRetriever getTracingTraceRetriever() {
    return tracingTraceRetriever;
  }

  private @Nullable OriginatorRetriever getOriginatorRetriever() {
    return originatorRetriever;
  }

  private @Nullable CallerRetriever getCallerRetriever() {
    return callerRetriever;
  }
  //endregion Getters & Setters
}
