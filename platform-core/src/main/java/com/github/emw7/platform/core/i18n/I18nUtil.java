package com.github.emw7.platform.core.i18n;

import java.util.IllformedLocaleException;
import java.util.Locale;
import org.springframework.lang.Nullable;

public final class I18nUtil {

  //region API

  /**
   * Returns the locale corresponding to the specified language tag.
   * <p>
   * The method can return {@code null} if either {@code languageTag} is {@code null} or if
   * `{@code Locale.Builder().setLanguageTag(languageTag)}` throws
   * {@link IllformedLocaleException}.
   *
   * @param languageTag IETF BCP 47 language tag
   * @return the locale corresponding to the specified language tag.
   */
  public static @Nullable Locale locale(@Nullable final String languageTag) {
    if (languageTag == null) {
      return null;
    }
    // else...
    try {
      return new Locale.Builder().setLanguageTag(languageTag).build();
    } catch (IllformedLocaleException e) {
      return null;
    }
  }
  //endregion API

  //region Constructors
  // prevents instantiation.
  private I18nUtil() {
  }
  //endregion Constructors

}
