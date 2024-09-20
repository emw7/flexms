package com.github.emw7.platform.i18n;

import com.github.emw7.platform.i18n.autoconfig.PlatformI18nAutoConfig;
import com.github.emw7.platform.i18n.util.I18nUtil;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Translates using a {@link MessageSource}.
 * <p>
 * {@link PlatformI18nAutoConfig#messageSourceTranslator(MessageSource, I18nLabelPrefixes)} define a
 * bean of such a type passing as arguments the {@link CompositeMessageSource} that must be
 * configured by the application and the bean of type {@code I18nLabelPrefixes} bean defined by
 * {@link PlatformI18nAutoConfig#i18nLabelPrefixes(String)}. Please refer to
 * <a href="https://github.com/emw7/flexms/blob/main/README.md">EMW7 reference
 * documentation</a> for a detailed description.
 * <p>
 * Regardless of {@link PlatformI18nAutoConfig} this class work with any {@link MessageSource} and
 * implementation (and {@link I18nLabelPrefixes} instance).
 * <p>
 * Refer to {@link #translate(Locale, String, Map)} for the details on the logic flow.
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

  /**
   * The translation related to the provided {@code label} and {@code locale} formatted with actual
   * values specified in {@code params}.
   * <p>
   * The implemented logic flow is better understandable splitting in two cases:
   * <ul>
   * <li><Case 1) {@code label} starts with EMW7 platform prefix (
   * {@link I18nLabelPrefixes#PLATFORM_PREFIX}) and custom prefix
   * {@link I18nLabelPrefixes#getCustomPrefix()} is not either null nor empty.</li>
   * <li>Case 2) {@code label} does not start with EMW7 platform prefix.</li>
   * </ul>
   * <b>Case 1)</b><br/>
   * In such a case first is tried to retrieve the translation for the supplied label with the
   * EMW7 platform prefix replaced by the custom prefix.
   * If such a translation does not exist then try for the provided label.
   * <b>Case 2)</b><br/>
   * In such a case only for the provided label is used to retrieve the translation.
   * <p>
   * It after case 1 and case 2 the translation cannot be found then the provided label is
   * returned.<br/>
   * Otherwise, the retrieved message is parsed to replace the placeholders with actual values.
   * Message formatting (placeholders replacement) is not delegated to message source but is done
   * through {@link StringSubstitutor} with these settings:
   * <ul>
   * <li>escape char: {@code \}.</li>
   * <li>variable prefix: <code>{</code>.</li>
   * <li>variable suffix: <code>}</code>.</li>
   * </ul>
   * <b>Example-A</b>
   * <pre>
   * Input:
   *   label = app.message.user
   *   params = { 'username' => 'john.doe', 'age': 33, 'active': true, 'riskFactor': 3.14 }
   *   locale = Locale.of("es");
   *   messages_es.properties => app.message.user = El usuario {username} tiene {age} años, está activo {active} y tiene un factor de riesgo de {riskFactor}
   * Output:
   *  translate(locale, label, params) => El usuario john.doe tiene 33 años, está activo true y tiene un factor de riesgo de 3.14
   * </pre>
   *
   * @param locale locale for which translation of {@code label} is wanted; set to
   *               {@code Locale.default()} is {@code null}
   * @param label  label for which translation is wanted
   * @param params actual parameters for message's placeholder
   * @return the translation related to the provided {@code label} and {@code locale} formatted with
   * actual values specified in {@code params}
   */
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

  /**
   * Returns {@code translate(I18nUtil.locale(language), label, params)}.
   *
   * @param language IETF BCP 47 language tag (refer to {@link I18nUtil#locale(String)} for more
   *                 information.
   * @param label    label for which translation is wanted
   * @param params   actual parameters for message's placeholder
   * @return {code translate(I18nUtil.locale(language), label, params)}
   * @see #translate(Locale, String, Map)
   */
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
  private @NonNull String retrieve(@NonNull final Locale locale, @NonNull final String label)
      throws NoSuchElementException {
    if (StringUtils.isEmpty(customPrefix()) || !label.startsWith(platformPrefix())) {
      // Case 2)
      // in this case label can be in the plain or in the platform form only (that is either does not
      //  or starts with platform prefix).
      return Optional.ofNullable(retrieveForPrefix(locale, label)).orElseThrow();
    }
    // else... label starts with platform prefix and custom prefix has been specified:
    //  try for custom prefix first and if it cannot be found, then try for platform prefix.
    // Case 1)
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
