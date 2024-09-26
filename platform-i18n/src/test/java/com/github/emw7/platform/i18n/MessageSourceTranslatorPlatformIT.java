package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;

// https://stackoverflow.com/a/58441082
@SpringBootTest(classes = MessageSourceTranslatorPlatformIT.class)
@EnableAutoConfiguration
@Import(CompositeMessageSourceTestConfig.class)
public class MessageSourceTranslatorPlatformIT {

  @BeforeAll
  public static void init() {
    Locale.setDefault(Locale.ROOT);
  }

  private final MessageSourceTranslator translator;

  public MessageSourceTranslatorPlatformIT(
      @Autowired @NonNull final MessageSourceTranslator translator) {
    this.translator = translator;
  }

  protected MessageSourceTranslator getTranslator() {
    return translator;
  }

  @Test
  public void givenNonExistentLabel_WhenTranslate_thenLabel() {
    final String translation = getTranslator().translate((Locale) null, "hUQu0xNy9w.Zslz9NnUyY", null);
    Assertions.assertThat(translation).as("given non-existent label when translate then label")
        .isEqualTo("hUQu0xNy9w.Zslz9NnUyY");
  }

  @Test
  public void givenLabel_WhenTranslateForNonExistentLanguage_thenFallbackToDefaultLanguage() {
    final String translation = getTranslator().translate(Locale.SIMPLIFIED_CHINESE,
        "com.github.emw7.platform.discovery.api.test.label.unknown", null);
    Assertions.assertThat(translation)
        .as("given label when translate for non existent language then fallback to default language")
        .isEqualTo("unknown");
  }

  @Test
  public void givenLabel_WhenTranslateForSpanish_thenSpanish() {
    final String translation = getTranslator().translate(Locale.forLanguageTag("es"),
        "com.github.emw7.platform.discovery.api.test.label.unknown", null);
    Assertions.assertThat(translation)
        .as("given label when translate for non existent language then fallback to default language")
        .isEqualTo("desconocido");
  }

  @Test
  public void givenLabelWithParams_WhenTranslate_thenParamsAreReplaced() {
    final String translation = getTranslator().translate((Locale) null,
        "com.github.emw7.platform.discovery.api.test.label.message",
        Map.of("type", "integration test", "project", "platform-i18n"));
    Assertions.assertThat(translation)
        .as("given label with params when translate then params are replaced")
        .isEqualTo("this is a integration test for platform-i18n");
  }

  @Test
  public void givenLabelWithInterpolation_WhenTranslate_thenInterpolated() {
    final String translation = getTranslator().translate((Locale) null,
        "com.github.emw7.platform.discovery.api.test.label.interpolation", null);
    Assertions.assertThat(translation)
        .as("given label with interpolation when translate then interpolated").isEqualTo("with love");
  }

  @Test
  public void givenLabelWithParamsAndInterpolation_WhenTranslate_thenParamsAreReplacesAndInterpolated() {
    final String translation = getTranslator().translate((Locale) null,
        "com.github.emw7.platform.discovery.api.test.label.message-interpolation",
        Map.of("type", "integration test", "project", "platform-i18n"));
    Assertions.assertThat(translation)
        .as("givenl label with params and interpolation when translate then params are replaces and interpolated")
        .isEqualTo("this is a integration test for platform-i18n with love");
  }

  @Test
  public void givenPlainLabel_WhenTranslate_thenPlainRetrieved() {
    final String translation = getTranslator().translate((Locale) null, "plain.test.label.foo", null);
    Assertions.assertThat(translation).as("given plain label when translate then retrieved")
        .isEqualTo("plain foo");
  }

  @Test
  public void givenCustomLabel_WhenTranslate_thenCustomOrPlatformRetrieved() {
    final String translation = getTranslator().translate((Locale) null,
        I18nLabelPrefixes.PLATFORM_PREFIX + "test.label.foo", null);
    Assertions.assertThat(translation)
        .as("given custom label when translate then platform retrieved").isEqualTo("platform foo");
  }

}
