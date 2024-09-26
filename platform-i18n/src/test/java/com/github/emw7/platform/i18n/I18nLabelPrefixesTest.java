package com.github.emw7.platform.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class I18nLabelPrefixesTest {

  @Test
  void noCustomPrefix() {
    I18nLabelPrefixes sut = new I18nLabelPrefixes("");
    Assertions.assertThat(sut.getPlatformPrefix())
        .as("no custom prefix :: platform prefix as default")
        .isEqualTo("com.github.emw7.platform.");
    Assertions.assertThat(sut.getCustomPrefix())
        .as("no custom prefix :: custom prefix empty string").isEqualTo("");
  }

  @Test
  void customPrefixWithoutDot() {
    I18nLabelPrefixes sut = new I18nLabelPrefixes("test.foo");
    Assertions.assertThat(sut.getPlatformPrefix())
        .as("custom prefix without dot :: platform prefix as default")
        .isEqualTo("com.github.emw7.platform.");
    Assertions.assertThat(sut.getCustomPrefix())
        .as("custom prefix without dot :: custom prefix with dot appended").isEqualTo("test.foo.");
  }


  @Test
  void customPrefixWithDot() {
    I18nLabelPrefixes sut = new I18nLabelPrefixes("test.foo.");
    Assertions.assertThat(sut.getPlatformPrefix())
        .as("custom prefix without dot :: platform prefix as default")
        .isEqualTo("com.github.emw7.platform.");
    Assertions.assertThat(sut.getCustomPrefix())
        .as("custom prefix without dot :: custom prefix as supplied").isEqualTo("test.foo.");
  }
}