package com.github.emw7.flexms.platform.i18n.autoconfig;

import com.github.emw7.flexms.platform.i18n.Translator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformI18nAutoConfig {


  // a useless translator.
  @ConditionalOnMissingBean
  @Bean
  Translator translator () {
    return new Translator() {
      @Override
      public String translate(@NonNull final String language, @NonNull final String label) {
        return label;
      }
    };
  }

}
