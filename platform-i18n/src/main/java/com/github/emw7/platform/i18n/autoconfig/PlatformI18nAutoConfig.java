package com.github.emw7.platform.i18n.autoconfig;

import com.github.emw7.platform.core.i18n.I18nUtil;
import com.github.emw7.platform.core.mapper.MapMapper;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import com.github.emw7.platform.i18n.MessageSourceTranslator;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.i18n.TranslatorContainer;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@AutoConfiguration
public class PlatformI18nAutoConfig {

  //region Beans
  @Bean
  public I18nLabelPrefixes i18nLabelPrefixes(
      @Value("${com.github.emw7.platform.i18n.label-custom-prefix:}") @NonNull final String customPrefix) {
    return new I18nLabelPrefixes(customPrefix);
  }

//  @Bean
//  public I18nLabelPrefixesContainer i18nLabelPrefixesContainer(
//      @NonNull final I18nLabelPrefixes target) {
//    //noinspection InstantiationOfUtilityClass
//    return new I18nLabelPrefixesContainer(target);
//  }

  @ConditionalOnBean(MessageSource.class)
  @Bean
  public Translator messageSourceTranslator(@NonNull final MessageSource messageSource,
      @NonNull final I18nLabelPrefixes i18nLabelPrefixes) {
    return new MessageSourceTranslator(messageSource, i18nLabelPrefixes);
  }

  /**
   * A useless default fallback translator that simply returns the specified label.
   */
  @ConditionalOnMissingBean
  @Bean
  public Translator translator() {
    return new Translator() {
      @Override
      public @NonNull String translate(@Nullable final Locale locale, @NonNull final String label,
          @Nullable Map<String, Object> params) {
        return (locale == null) ? Locale.getDefault().getLanguage()
            : locale.getLanguage() + ':' + label + '[' + MapMapper.mapToString(params) + ']';
      }

      @Override
      public @NonNull String translate(@Nullable final String language, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return translate(I18nUtil.locale(language), label, params);
      }
    };
  }

  @Bean
  public TranslatorContainer translatorContainer(@NonNull final Translator translator) {
    //noinspection InstantiationOfUtilityClass
    return new TranslatorContainer(translator);
  }
  //endregion Beans

//  //region Public type: I18nLabelPrefixes container
//  // https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/
//  //  section '4. OPTION 2: IMPLEMENT PROVIDER'.
//  public static final class I18nLabelPrefixesContainer {
//
//    private static I18nLabelPrefixes target;
//
//    // returns null is called before any object instantiation
//    public static I18nLabelPrefixes get() {
//      return target;
//    }
//
//    // if called programmatically can override translator.
//    private I18nLabelPrefixesContainer(@NonNull final I18nLabelPrefixes target) {
//      I18nLabelPrefixesContainer.target = target;
//    }
//
//  }
//  //endregion Public type: I18nLabelPrefixes container

//  //region Public type: Translator container
//  // https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/
//  //  section '4. OPTION 2: IMPLEMENT PROVIDER'.
//  public static final class TranslatorContainer {
//
//    private static Translator translator;
//
//    // returns null is called before any object instantiation
//    public static Translator getTranslator() {
//      return translator;
//    }
//
//    // if called programmatically can override translator.
//    private TranslatorContainer(@NonNull final Translator translator) {
//      TranslatorContainer.translator = translator;
//    }
//
//  }
  //endregion Public type: Translator container

}
