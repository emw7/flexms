package com.github.emw7.platform.i18n;

import com.github.emw7.platform.core.i18n.I18nUtil;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Translates using a {@link MessageSource}.
 * <p>
 * Example-A
 * <pre>
 * Input:
 *   label = app.message.user
 *   params = { 'username' => 'john.doe', 'age': 33, 'active': true, 'riskFactor': 3.14 }
 *   locale = Locale.of("es");
 *   messages_es.properties => app.message.user = El usuario {username} tiene {age} años, está activo {active} y tiene un factor de riesgo de {riskFactor}
 * Output:
 *  translate(locale, label, params) => El usuario john.doe tiene 33 años, está activo true y tiene un factor de riesgo de 3.14
 * </pre>
 */
public class MessageSourceTranslator implements Translator {

  //region Private properties
  private final MessageSource messageSource;
  private final I18nLabelPrefixes i18nLabelPrefixes;
  //endregion Private properties

  //region Constructors
  public MessageSourceTranslator(@NonNull final MessageSource messageSource,
      @NonNull final I18nLabelPrefixes i18nLabelPrefixes) {
    this.messageSource = messageSource;
    this.i18nLabelPrefixes = i18nLabelPrefixes;
  }
  //endregion Constructors

  //region API
  @Override
  public @NonNull String translate(@Nullable final Locale locale, @NonNull final String label,
      @Nullable final Map<String, Object> params) {
    final Locale _locale = Optional.ofNullable(locale).orElse(Locale.getDefault());
    String translatedMessage = null;
    try {
      translatedMessage = retrieve(_locale, label);
    } catch (NoSuchElementException e) {
      return label;
    }

    StringSubstitutor sub = new StringSubstitutor(
        StringLookupFactory.INSTANCE.interpolatorStringLookup(params));
    sub.setEscapeChar('\\');
    sub.setVariablePrefix('{');
    sub.setVariableSuffix('}');
    translatedMessage = sub.replace(translatedMessage);
    return translatedMessage;
  }

  @Override
  public @NonNull String translate(@Nullable final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params) {
    return translate(I18nUtil.locale(language), label, params);
  }
  //endregion API

  //region Getters & Setters
  private @NonNull MessageSource getMessageSource() {
    return messageSource;
  }

  private @NonNull I18nLabelPrefixes getI18nLabelPrefixes() {
    return i18nLabelPrefixes;
  }

  private @NonNull String platformPrefix() {
    return getI18nLabelPrefixes().getPlatformPrefix();
  }

  private @NonNull String customPrefix() {
    return getI18nLabelPrefixes().getCustomPrefix();
  }
  //endregion Getters & Setters

  //region Private methods

  /**
   * @param locale
   * @param label
   * @return
   * @throws NoSuchElementException if label in the form of plain, custom or platform cannot be
   *                                found
   */
  private @NonNull String retrieve(@NonNull final Locale locale, @NonNull final String label)
      throws NoSuchElementException {
    if (StringUtils.isEmpty(customPrefix()) || !label.startsWith(platformPrefix())) {
      // in this case label can be in the plain or in the platform form only (that is either does not
      //  or starts with platform prefix).
      return Optional.ofNullable(retrieveForPrefix(locale, label)).orElseThrow();
    }
    // else... label starts with platform prefix and custom prefix has been specified:
    //  try for custom prefix first and if it cannot be found, then try for platform prefix.
    final String customLabel = customPrefix() + label.substring(platformPrefix().length());
    return Optional.ofNullable(retrieveForPrefix(locale, customLabel))
        .or(() -> Optional.ofNullable(retrieveForPrefix(locale, label))).orElseThrow();
  }

  private @Nullable String retrieveForPrefix(@NonNull final Locale locale,
      @NonNull final String label) {
    try {
           return getMessageSource().getMessage(label, null, locale);
    } catch (NoSuchMessageException e) {
      return null;
    }
  }
  //endregion Private methods

}
