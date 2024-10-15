package com.github.emw7.platform.log;

import com.github.emw7.platform.core.Constants;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Representation of an argument [ARG], that is, a (name, value) pair, with name of type
 * {@link String} and value of generic type.
 *
 * @param <T> type of the value
 */
public final class Arg<T> {

  //region Public static methods

  /**
   * Returns an instance representing the arg with the provided name and value.
   *
   * @param name name of the arg
   * @param value value of the arg
   * @return an instance representing the arg with the provided name and value
   * @param <T> type of the value
   */
  public static <T> @NonNull Arg<T> of(@NonNull final String name, @Nullable final T value) {
    return new Arg<>(name, value);
  }
  //endregion Public static methods

  //region Private properties
  private final String name;
  private final T value;
  //endregion Private properties

  //region Constructors
  // prevents instantiation, instantiate with of(...).
  private Arg(@NonNull final String name, @Nullable final T value) {
    this.name = name;
    this.value = value;
  }
  //endregion Constructors

  //region API

  /**
   * Returns the name of this arg.
   *
   * @return the name of this arg
   */
  public @NonNull String name() {
    return name;
  }

  /**
   * Returns the string representation of the value this arg, returning
   * {@value Constants#NULL_STRING_REPRESENTATION} if the value is {@code null}.
   * <p>
   * The string representation is got as (if value is not {@code null} `value.toString()`.
   *
   * @return the string representation of the value this arg, returning
   *         {@value Constants#NULL_STRING_REPRESENTATION} if the value is {@code null}
   */
  public @NonNull String asString() {
    return (value == null) ? Constants.NULL_STRING_REPRESENTATION : value.toString();
  }
  //endregion API

  //region Overrides from Object

  /**
   * Two args are equals if they have the same (case-insensitive) name.
   * <p>
   * <pre>
   * if (this == obj) {
   *   return true;
   * } else if (obj == null) {
   *   return false;
   * } else if (getClass() != obj.getClass()) {
   *   return false;
   * } else {
   *   return name.equalsIgnoreCase(((Arg<?>) obj).name());
   * }
   * </pre>
   *
   * @param obj the reference object with which to compare.
   * @return true if this object is the same as the obj argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (getClass() != obj.getClass()) {
      return false;
    } else {
      return name.equalsIgnoreCase(((Arg<?>) obj).name());
    }
  }

  /**
   * Returns {@code name.hashCode()}.
   *
   * @return {@code name.hashCode()}
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Returns the string got as {@code name + '=' + asString()}.
   * <p>
   * The returned value is different from the one returned by {@link #asString()}.
   * <p>
   * Example-A:
   * <pre>
   * name: section
   * value: 123
   * => section=123
   * </pre>
   *
   * @return the string got as {@code name + '=' + asString()}
   */
  @Override
  public @NonNull String toString() {
    return name + '=' + asString();
  }
  //endregion Overrides from Object

}
