package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class I18nEnabledRuntimeException extends RuntimeException {

  //region Private static methods.
  private static @NonNull String messageFromLabel(@Nullable final Locale locale,
      @NonNull final String label, @Nullable Map<String, Object> params) {
    return TranslatorContainer.getTranslator().translate(locale, label, params);
  }
  //endregion Private static methods.

  public I18nEnabledRuntimeException(@Nullable final Locale locale, @NonNull final String label,
      @Nullable Map<String, Object> params, final Throwable cause, final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(messageFromLabel(locale, label, params), cause, enableSuppression, writableStackTrace);
  }

  public I18nEnabledRuntimeException(@Nullable final Locale locale, @NonNull final String label,
      @Nullable Map<String, Object> params, @Nullable final Throwable cause) {
    super(messageFromLabel(locale, label, params), cause);
  }

  public I18nEnabledRuntimeException(@Nullable final Locale locale, @NonNull final String label,
      @Nullable Map<String, Object> params) {
    super(messageFromLabel(locale, label, params));
  }

  public I18nEnabledRuntimeException(final Throwable cause) {
    super(cause);
  }

  public I18nEnabledRuntimeException() {
    super();
  }

}
