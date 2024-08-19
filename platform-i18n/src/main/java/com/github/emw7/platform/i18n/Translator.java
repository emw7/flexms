package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface Translator {

  @NonNull String translate (@Nullable final Locale locale, @NonNull final String label,
      @Nullable final Map<String, Object> params);

  @NonNull String translate(@Nullable final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params);

}
