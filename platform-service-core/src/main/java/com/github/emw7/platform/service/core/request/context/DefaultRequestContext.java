package com.github.emw7.platform.service.core.request.context;

import java.util.Locale;
import org.springframework.lang.NonNull;

// record
/**
 * REV:V
 * @see RequestContext
 */
public final class DefaultRequestContext implements RequestContext {

  private final Locale locale;
  private final Originator originator;
  private final Caller caller;
  //private final MultiValueMap<String, Object> attributes;

  /**
   * If {@code originator} is equal to {@link Originator#DEFAULT} then {@code originator} is forced
   * to be equal to {@code caller}.
   *
   * @param locale
   * @param originator
   * @param caller
   */
  public DefaultRequestContext(@NonNull final Locale locale,
      @NonNull final Originator originator, @NonNull final Caller caller/*,
      final MultiValueMap<String, Object> attributes*/) {
    this.locale = locale;
    this.originator = (originator == Originator.DEFAULT) ? new Originator.Builder(caller).build() : originator;
    this.caller = caller;
    /*this.attributes = attributes;*/
  }

  @Override
  public @NonNull Locale locale() {
    return locale;
  }

  @Override
  public @NonNull Originator originator() {
    return originator;
  }

  @Override
  public @NonNull Caller caller() {
    return caller;
  }

  /*  public @NonNull List<Object> getAttributes(String name) {
    return Optional.ofNullable(attributes).orElse(new LinkedMultiValueMap<>()).getOrDefault(name, List.of());
  }*/

}
