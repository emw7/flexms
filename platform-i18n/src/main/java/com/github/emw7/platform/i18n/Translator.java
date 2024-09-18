package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Interfaces for translator that is classes that given a locale, a label and parameters returns
 * the translation fo the label for the language represented by the locale with placeholders in
 * the message replaced by actual values present in params.
 * <p>
 * <b>Example-A</b>
 * <pre>
 * Input:
 *   locale language: es
 *   label: com.github.emw7.i18n.an-example-label
 *   params: codeNumber => 7, codeStr => EMW
 *   message for the label in locale language is: una etiqueta de ejemplo de {codeStr}{codeNumber}
 * Action:
 *   translate(es, com.github.emw7.i18n.an-example-label, codeNumber => 7, codeStr => EMW)
 * Result:
 *   una etiqueta de ejemplo de EMW7
 * </pre>
 */
public interface Translator {

  @NonNull String translate (@Nullable final Locale locale, @NonNull final String label,
      @Nullable final Map<String, Object> params);

  /**
   * Could be implemented as {@code translate(I18nUtil.locale(language), label, params)}.
   */
  @NonNull String translate(@Nullable final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params);

}
