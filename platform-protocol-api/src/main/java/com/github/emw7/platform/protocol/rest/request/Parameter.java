package com.github.emw7.platform.protocol.rest.request;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class Parameter<T> {

  public static <T> Parameter<T> of (@NonNull final String name) {
    return new Parameter<>(name);
  }

  public static <T> Parameter<T> of (@NonNull final String name, @Nullable final T defaultValue) {
    return new Parameter<>(name, false, defaultValue);
  }

  private final String name;
  private final boolean isMandatory;
  private final T defaultValue;

  public Parameter(@NonNull final String name) {
    this(name, true, null);
  }

  private Parameter(@NonNull final String name, final boolean isMandatory, @Nullable final T defaultValue) {
    this.name= name;
    this.isMandatory= isMandatory;
    this.defaultValue= defaultValue;
  }

  public @NonNull String getName() {
    return name;
  }

  public boolean isMandatory() {
    return isMandatory;
  }

  public @Nullable T getDefaultValue() {
    return defaultValue;
  }
}
