package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.telemetry.tracing.Tracing;
import java.util.Locale;
import org.springframework.lang.NonNull;

// record
public final class DefaultRequestContext implements RequestContext {

  private final @NonNull Locale locale;
  private final @NonNull Tracing tracing;
  private final @NonNull Caller originator;
  private final @NonNull Caller caller;
  //private final MultiValueMap<String, Object> attributes;

  public DefaultRequestContext(@NonNull final Locale locale, @NonNull final Tracing tracing,
      @NonNull final Caller originator, @NonNull final Caller caller/*,
      final MultiValueMap<String, Object> attributes*/) {
    this.locale = locale;
    this.tracing = tracing;
    this.originator = originator;
    this.caller = caller;
    /*this.attributes = attributes;*/
  }

  public @NonNull Locale locale() {
    return locale;
  }

  @Override
  public @NonNull Tracing tracing() {
    return tracing;
  }

  /**
   * Returns {@link #caller()} if {@code originator == Caller#DEFAULT}; {@code originator} otherwise.
   * @return {@link #caller()} if {@code originator == Caller#DEFAULT}; {@code originator} otherwise
   */
  @Override
  public @NonNull Caller originator() {
    return ( originator == Caller.DEFAULT ) ? caller() : originator;
  }

  @Override
  public @NonNull Caller caller() {
    return caller;
  }

  /*  public @NonNull List<Object> getAttributes(String name) {
    return Optional.ofNullable(attributes).orElse(new LinkedMultiValueMap<>()).getOrDefault(name, List.of());
  }*/

}
