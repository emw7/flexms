package com.github.emw7.platform.i18n;

import com.github.emw7.platform.i18n.util.I18nUtil;
import com.github.emw7.platform.core.mapper.MapMapper;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * A useless default fallback translator that does not translate bu returns a string composed by
 * parameter supplied to {@code translate} methods.
 * <p>
 * This is created by {@link com.github.emw7.platform.i18n.autoconfig.PlatformI18nAutoConfig} in case
 * no other translator bean is available.
 */
public class FooTranslator implements Translator {

  /**
   * Returns &lt;locale-language&gt;:{code label}[&lt;string representation of {@code params}&gt;].
   * <p>
   * If {@code locale} is {@code null} then {@code Locale.getDefault()} is used.
   * 
   * @param locale locale from which retrieve language for which translation of {@code label} is wanted
   * @param label label for which translation is wanted
   * @param params actual parameters for message's placeholder
   *
   * @return &lt;locale-language&gt;:{code label}[&lt;string representation of {@code params}&gt;]
   */
  @Override
  public @NonNull String translate(@Nullable final Locale locale, @NonNull final String label,
      @Nullable Map<String, Object> params) {
    return ((locale == null) ? Locale.getDefault().getLanguage()
        : locale.getLanguage()) + ':' + label + '[' + MapMapper.mapToString(params) + ']';
  }

  /**
   * Returns the result of {@code translate(I18nUtil.locale(language), label, params)}.
   *
   * @param language language for which translation of {@code label} is wanted
   * @param label label for which translation is wanted
   * @param params actual parameters for message's placeholder
   *
   * @return the result of {@code translate(I18nUtil.locale(language), label, params)}
   * 
   * @see I18nUtil#locale(String) 
   */
  @Override
  public @NonNull String translate(@Nullable final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params) {
    return translate(I18nUtil.locale(language), label, params);
  }
}
