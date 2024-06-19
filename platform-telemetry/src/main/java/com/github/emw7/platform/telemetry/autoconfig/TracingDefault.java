package com.github.emw7.platform.telemetry.autoconfig;

import com.github.emw7.platform.telemetry.tracing.Span;
import com.github.emw7.platform.telemetry.tracing.Trace;
import com.github.emw7.platform.telemetry.tracing.Tracing;
import java.util.UUID;
import org.springframework.lang.NonNull;


// only auto-configuration can create this class; that is why constructors are package private.
// LIMITATION: allow for 1 span only!
class TracingDefault implements Tracing {

  //region Private properties
  private final Trace trace;
  //endregion Private methods

  //region Constructors
  TracingDefault() {
    this(UUID.randomUUID().toString());
  }

  TracingDefault(@NonNull final String traceId) {
    this.trace = new Trace(traceId);
    this.trace.addSpan(new Span());
  }
  //endregion Constructors

  //region API
  @Override
  public @NonNull String traceId() {
    return trace.getId();
  }

  @Override
  public @NonNull String spanId() {
    return trace.getSpans().getFirst().getId();
  }

  @Override
  public @NonNull Trace trace() {
    return trace;
  }

  @Override
  public Span addSpan() {
    return null;
  }

  //endregion API

  //region Getters & Setters
  //endregion Getters & Setters

}
