package com.github.emw7.platform.core.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;

public final class ArrayUtil {

  //region API

  /**
   * Returns a new array which is the result of appending {@code b} to {@code a}.
   *
   * @param a the first array
   * @param b the second array (the one to be appended to the first)
   * @return a new array which is the result of appending {@code b} to {@code a}
   */
  public static @Nullable Object[] join(@Nullable Object[] a, @Nullable Object[] b) {
    if (a == null && b == null) {
      return null;
    } else if (a == null) {
      // b is NOT null... use b only.
      final Object[] ret = new Object[b.length];
      System.arraycopy(b, 0, ret, 0, b.length);
      return ret;
    } else if (b == null) {
      // a is NOT null... use a only.
      final Object[] ret = new Object[a.length];
      System.arraycopy(a, 0, ret, 0, a.length);
      return ret;
    } else {
      // both a and b are NOT null
      final Object[] ret = new Object[a.length + b.length];
      System.arraycopy(a, 0, ret, 0, a.length);
      System.arraycopy(b, 0, ret, a.length, b.length);
      return ret;
    }
  }

  /**
   * See {@link java.util.Arrays#copyOfRange(Object[], int, int)}:
   * <ul>
   * <li>returns {@code null} in case of {@link NullPointerException}</li>
   * <li>returns empty array in case of {@link ArrayIndexOutOfBoundsException} and {@link IllegalArgumentException}</li>
   * </ul>
   *
   * @param original
   * @param from
   * @param to
   * @return
   * @param <T>
   */
  @SuppressWarnings("unchecked")
  public static @Nullable <T> T[] slice (@Nullable final T[] original, int from, int to) {
    if ( original == null ) {
      return null;
    }
    try {
      return Arrays.copyOfRange(original, from, to);
    } catch ( ArrayIndexOutOfBoundsException | IllegalArgumentException e ) {
      return (T[]) new Object[0];
    }
  }
  //endregion API

  //region Constructors
  // prevents instantiation.
  private ArrayUtil() {
  }
  //endregion Constructors

}
