package com.github.emw7.platform.i18n;

import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface Translator {

  String translate(@NonNull final String language, @NonNull final String label);

  String translate(@NonNull final String label);

  String translate(@NonNull final String language, @NonNull final String label,
      @Nullable final Map<String, Object> params);

  String translate(@NonNull final String label, @Nullable final Map<String, Object> params);

}
