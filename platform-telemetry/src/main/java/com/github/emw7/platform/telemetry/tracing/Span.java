package com.github.emw7.platform.telemetry.tracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public final class Span {

  private final String id;

  private final List<Span> children;

  public Span() {
    this.id = UUID.randomUUID().toString();
    this.children = new ArrayList<>();
  }

  public void addChild(@NonNull final Span span) {
    children.add(span);
  }

  /**
   * Returns an unmodifiable view of the children of this span.
   *
   * @return an unmodifiable view of the children of this span
   */
  public @NonNull List<Span> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public @NonNull String getId() {
    return id;
  }
}
