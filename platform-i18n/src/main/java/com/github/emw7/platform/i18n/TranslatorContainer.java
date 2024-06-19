package com.github.emw7.platform.i18n;

import org.springframework.lang.NonNull;

// https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/
//  section '4. OPTION 2: IMPLEMENT PROVIDER'.
public final class TranslatorContainer {

  private static Translator translator;

  // returns null is called before any object instantiation
  public static Translator getTranslator() {
    return translator;
  }

  // if called programmatically can override translator.
  public TranslatorContainer(@NonNull final Translator translator) {
    TranslatorContainer.translator = translator;
  }

}
