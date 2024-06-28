package com.github.emw7.platform.i18n.autoconfig;

import com.github.emw7.platform.core.mapper.MapMapper;
import com.github.emw7.platform.i18n.Translator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@AutoConfiguration
public class PlatformI18nAutoConfig {

  /**
   * A useless default fallback translator that simply returns the specified label.
   */
  @ConditionalOnMissingBean
  @Bean
  Translator translator() {
    return new Translator() {

      @Override
      public String translate(@NonNull final String language, @NonNull final String label) {
        return translate(label);
      }

      @Override
      public String translate(@NonNull final String label) {
        return translate(label, Map.of());
      }

      @Override
      public String translate(@NonNull final String language, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return language + ':' + label + '[' + MapMapper.mapToString(params) + ']';
      }

      @Override
      public String translate(@NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return label + '[' + Optional.ofNullable(params).orElse(Map.of()) + ']';
      }
    };
  }

  @Bean
  public TranslatorContainer translatorContainer(@NonNull final Translator translator) {
    return new TranslatorContainer(translator);
  }

  //region Translator container
  // https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/
  //  section '4. OPTION 2: IMPLEMENT PROVIDER'.
  public static final class TranslatorContainer {

    private static Translator translator;

    // returns null is called before any object instantiation
    public static Translator getTranslator() {
      return translator;
    }

    // if called programmatically can override translator.
    private TranslatorContainer(@NonNull final Translator translator) {
      TranslatorContainer.translator = translator;
    }

  }
  //endregion Translator container

}
