package com.github.emw7.platform.core.mapper;

import java.util.Locale;
import org.springframework.lang.Nullable;

public final class BooleanMapper {

  //region API

  /**
   * Returns {@code true} when specified string is any got these (case-insensitive): truen, 1, t,
   * on, yes, y; {@code false} otherwise
   *
   * @param b the string to map to {@code boolean}
   * @return {@code true} when specified string is any got these (case-insensitive): truen, 1, t,
   * on, yes, y; {@code false} otherwise
   */
  public static boolean fromString(@Nullable final String b) {
    if (b == null) {
      return false;
    }
    // else...

    return switch (b.toLowerCase(Locale.ROOT)) {
      case "true", "t", "1", "on", "yes", "y" -> true;
      default -> false;
    };
  }

  //endregion API

  //region Constructors
  // prevents instantiation.
  private BooleanMapper() {
  }
  //endregion Constructors
}
