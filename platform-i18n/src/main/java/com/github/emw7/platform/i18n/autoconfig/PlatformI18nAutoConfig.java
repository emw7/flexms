package com.github.emw7.platform.i18n.autoconfig;

import com.github.emw7.platform.i18n.FooTranslator;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import com.github.emw7.platform.i18n.MessageSourceTranslator;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.i18n.TranslatorContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformI18nAutoConfig {

  //region Beans
  @Bean
  public I18nLabelPrefixes i18nLabelPrefixes(
      @Value("${com.github.emw7.platform.i18n.label-custom-prefix:}") @NonNull final String customPrefix) {
    return new I18nLabelPrefixes(customPrefix);
  }

  @ConditionalOnBean(MessageSource.class)
  @Bean
  public Translator messageSourceTranslator(@NonNull final MessageSource messageSource,
      @NonNull final I18nLabelPrefixes i18nLabelPrefixes) {
    return new MessageSourceTranslator(messageSource, i18nLabelPrefixes);
  }

  /**
   * A useless default fallback translator that simply returns the specified label.
   * <p>
   * This is created to allow application works in case not {@code translator} has been
   * provided.
   */
  @ConditionalOnMissingBean(Translator.class)
  @Bean
  public FooTranslator translator() {
    return new FooTranslator();
  }

  @Bean
  public TranslatorContainer translatorContainer(@NonNull final Translator translator) {
    //noinspection InstantiationOfUtilityClass
    return new TranslatorContainer(translator);
  }
  //endregion Beans
}
