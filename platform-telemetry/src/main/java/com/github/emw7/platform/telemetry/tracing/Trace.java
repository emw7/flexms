package com.github.emw7.platform.telemetry.tracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public final class Trace {

  private final String id;

  private final List<Span> spans;

  public Trace() {
    this(UUID.randomUUID().toString());
  }

  public Trace(@NonNull final String id) {
    this.id = id;
    this.spans = new ArrayList<>();
  }

  public void addSpan(@NonNull final Span span) {
    spans.add(span);
  }

  /**
   * Returns an unmodifiable view of the spans of this trace.
   *
   * @return an unmodifiable view of the spans of this trace
   */
  public @NonNull List<Span> getSpans() {
    return Collections.unmodifiableList(spans);
  }

  public @NonNull String getId() {
    return id;
  }
}
