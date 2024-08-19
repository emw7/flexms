package com.github.emw7.platform.i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class I18nEnabledException extends Exception {

  //region Private static properties
  @I18nLabel(label = "com.github.emw7.platform.i18n.unknown-error-reason")
  private static final String I18N_LABEL_UNKNOWN_ERROR_REASON =
      I18nLabelPrefixes.PLATFORM_PREFIX + "i18n." + "unknown-error-reason";
  //endregion Private static properties

  //region Private static methods
  private static @NonNull Map<String, Object> addErrorReason(
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    final String errorReason = (cause == null) ? TranslatorContainer.getTranslator()
        .translate((Locale) null, I18N_LABEL_UNKNOWN_ERROR_REASON, null)
        : String.valueOf(cause.getLocalizedMessage());
    if (params == null) {
      return Map.of("errorReason", errorReason);
    } else {
      final Map<String, Object> paramsWithErrorReason = new HashMap<>(params);
      paramsWithErrorReason.put("errorReason", errorReason);
      return Collections.unmodifiableMap(paramsWithErrorReason);
    }
  }

  private static @NonNull String messageFromLabel(@NonNull final String label,
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    return TranslatorContainer.getTranslator()
        .translate((Locale) null, label, addErrorReason(params, cause));
  }
  //endregion Private static methods

  //region Constructors
  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace) {
    super(messageFromLabel(label, params, cause), cause, enableSuppression, writableStackTrace);
  }

  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    super(messageFromLabel(label, params, cause), cause);
  }

  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params) {
    super(messageFromLabel(label, params, null));
  }

  public I18nEnabledException(final Throwable cause) {
    super(cause);
  }

  public I18nEnabledException() {
    super();
  }
  //endregion Constructors

  //region Private methods
  //endregion Private methods

}
