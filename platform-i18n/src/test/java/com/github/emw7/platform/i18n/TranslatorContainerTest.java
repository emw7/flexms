package com.github.emw7.platform.i18n;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

class TranslatorContainerTest {

  // this is to reset the static translator property of the TranslatorContainer (the SUT)
  //  utility class.
  @BeforeEach
  public void reset() throws NoSuchFieldException, IllegalAccessException {
    final Field translator = TranslatorContainer.class.getDeclaredField("translator");
    translator.setAccessible(true);
    translator.set(null, null);
  }

  @Test
  public void getTranslatorReturnsTheTranslatorSuppliedToTheConstructor() {
    final Translator translator = new Translator() {
      @NonNull
      @Override
      public String translate(@Nullable final Locale locale, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return "";
      }

      @NonNull
      @Override
      public String translate(@Nullable final String language, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return "";
      }
    };

    new TranslatorContainer(translator);
    Assertions.assertThat(TranslatorContainer.getTranslator())
        .as("getTranslator returns the translator supplied to the constructor")
        .isSameAs(translator);
  }

  @Test
  public void getTranslatorReturnsFooTranslatorIfTranslatorIsNull() {
    Assertions.assertThat(TranslatorContainer.getTranslator())
        .as("getTranslator returns FooTranslator if translator is null")
        .isInstanceOf(FooTranslator.class);
  }

  @Test
  public void constructorOfTranslatorContainerIgnoresSuppliedTranslatorIfTranslatorIsNotNull() {
    final Translator translator = new Translator() {
      @NonNull
      @Override
      public String translate(@Nullable final Locale locale, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return "";
      }

      @NonNull
      @Override
      public String translate(@Nullable final String language, @NonNull final String label,
          @Nullable final Map<String, Object> params) {
        return "";
      }
    };

    new TranslatorContainer(translator);
    new TranslatorContainer(new FooTranslator());
    Assertions.assertThat(TranslatorContainer.getTranslator())
        .as("constructor of translator container ignores supplied translator if translator is not null")
        .isSameAs(translator);
  }

}
