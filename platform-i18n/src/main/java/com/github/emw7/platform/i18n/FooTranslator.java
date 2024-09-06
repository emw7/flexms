package com.github.emw7.platform.i18n;

import com.github.emw7.platform.core.i18n.I18nUtil;
import com.github.emw7.platform.core.mapper.MapMapper;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class FooTranslator implements Translator {

  @Override
  public @NonNull String translate(@Nullable final Locale locale, @NonNull final String label,
      @Nullable Map<String, Object> params) {
    return ((locale == null) ? Locale.getDefault().getLanguage()
        : locale.getLanguage()) + ':' + label + '[' + MapMapper.mapToString(params) + ']';
  }

  @Override
  public @NonNull String translate(@Nullable final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params) {
    return translate(I18nUtil.locale(language), label, params);
  }
}
