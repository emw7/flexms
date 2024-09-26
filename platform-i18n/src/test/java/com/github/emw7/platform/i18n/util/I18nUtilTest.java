package com.github.emw7.platform.i18n.util;

import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

class I18nUtilTest {

  @Test
  void localeFromNullGivesNull() {
    //noinspection ConstantValue
    final Locale sut= I18nUtil.locale(null);
    //noinspection ConstantValue
    Assertions.assertThat(sut).as("locale from null gives null").isNull();
  }

  @Test
  void localeFromNotAWellFormedBCP47TagGivesNull() {
    final Locale sut= I18nUtil.locale("emw7");
    Assertions.assertThat(sut).as("locale from not a well-formed BCP 47 tag gives null").isNull();
  }

  /**
   * From <a href="https://www.w3.org/International/articles/language-tags/">Language tags in HTML and XML</a>:<br/>
   * en	English	language<br/>
   * mas	Maasai	language<br/>
   * fr-CA	French as used in Canada	language+region<br/>
   * es-419	Spanish as used in Latin America	language+region<br/>
   * zh-Hans	Chinese written with Simplified script	language+script<br/>
   *<br/>
   * From <a href="https://en.wikipedia.org/wiki/Codes_for_constructed_languages">Codes for constructed languages</a>:<br/>
   * Interslavic	Latin	isv-Latn<br/>
   * Cyrillic	isv-Cyrl<br/>
   * Klingon	Latin	tlh-Latn<br/>
   * KLI pIqaD	tlh-Piqd<br/>
   * Lingua Franca Nova	Latin	lfn-Latn<br/>
   * Cyrillic	lfn-Cyrl<br/>
   * Quenya	Latin	qya-Latn<br/>
   * Tengwar	qya-Teng<br/>
   * Cirth	qya-Cirt<br/>
   * Sarati	qya-Sara<br/>
   * Sindarin	Latin	sjn-Latn<br/>
   * Tengwar	sjn-Teng<br/>
   * Cirth	sjn-Cirt<br/>
   */
  @Test
  void localeFromAWellFormedBCP47TagGivesCorrespondingLocale() {
    for (String tag : new String[]{"en", "mas", "fr-CA", "es-419", "zh-Hans", "isv-Latn",
        "isv-Cyrl", "tlh-Latn", "tlh-Piqd", "lfn-Latn", "lfn-Cyrl", "qya-Latn", "qya-Teng",
        "qya-Cirt", "qya-Sara", "sjn-Latn", "sjn-Teng", "sjn-Cirt"}) {
      final Locale sut = I18nUtil.locale(tag);
      Assertions.assertThat(sut).as("locale from %s gives not null", tag).isNotNull()
          .extracting(Locale::toLanguageTag, InstanceOfAssertFactories.STRING)
          .as("locale from %s gives corresponding language tag", tag).isEqualTo(tag);
    }
  }

}