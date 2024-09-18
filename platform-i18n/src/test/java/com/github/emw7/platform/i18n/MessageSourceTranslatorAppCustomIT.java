package com.github.emw7.platform.i18n;

import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;

// https://stackoverflow.com/a/58441082
@SpringBootTest(classes = MessageSourceTranslatorAppCustomIT.class,
properties = {"com.github.emw7.platform.i18n.label-custom-prefix=custom"})
@EnableAutoConfiguration
@Import({CompositeMessageSourceTestConfig.class, CustomAppMessageSourceTestConfig.class})
public class MessageSourceTranslatorAppCustomIT {

  @BeforeAll
  public static void init() {
    Locale.setDefault(Locale.ROOT);
  }

  private final MessageSourceTranslator translator;

  public MessageSourceTranslatorAppCustomIT(
      @Autowired @NonNull final MessageSourceTranslator translator) {
    this.translator = translator;
  }

  protected MessageSourceTranslator getTranslator() {
    return translator;
  }
  @Test
  public void givenPlainLabel_WhenTranslate_thenPlainRetrievedFromAppMessageSource() {
    final String translation = getTranslator().translate((Locale) null, "plain.test.label.foo", null);
    Assertions.assertThat(translation).as("given plain label when translate then retrieved")
        .isEqualTo("app foo");
  }

}
