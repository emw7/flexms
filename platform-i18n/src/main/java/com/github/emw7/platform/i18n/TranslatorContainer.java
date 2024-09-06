package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/
//  section '4. OPTION 2: IMPLEMENT PROVIDER'.
public final class TranslatorContainer {

  private static Translator translator;

  /**
   * Returns either {@link #translator} ot {@link FooTranslator} if {@link #translator} is {@code null}.
   * @return either {@link #translator} ot {@link FooTranslator} if {@link #translator} is {@code null}
   */
  // returns null is called before any object instantiation
  public static @NonNull Translator getTranslator() {
    // TODO put a formal log about translator is null... as for log JSON-PROCESSING
    return translator != null ? translator : new FooTranslator();
  }

  // if called programmatically can override translator.
  public TranslatorContainer(@NonNull final Translator translator) {
    TranslatorContainer.translator = translator;
  }

}
