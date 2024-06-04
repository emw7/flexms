package com.github.emw7.flexms.platform.i18n;

import org.springframework.lang.NonNull;

public interface Translator {

  String translate (@NonNull final String language, @NonNull final String label);

}
