package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;

// https://stackoverflow.com/a/58441082
@SpringBootTest(classes = MessageSourceTranslatorCustomIT.class,
properties = {"com.github.emw7.platform.i18n.label-custom-prefix=custom"})
@EnableAutoConfiguration
public class MessageSourceTranslatorCustomIT extends MessageSourceTranslatorPlatformIT {

  @BeforeAll
  public static void init ()
  {
    Locale.setDefault(Locale.ROOT);
  }

  public MessageSourceTranslatorCustomIT(@Autowired @NonNull final MessageSourceTranslator translator) {
    super(translator);
  }

  @Test
  public void givenCustomLabel_WhenTranslate_thenCustomOrPlatformRetrieved() {
    final String translation = getTranslator().translate((Locale) null,

        I18nLabelPrefixes.PLATFORM_PREFIX + "test.label.foo", null);
    Assertions.assertThat(translation).as("given custom label when translate then custom retrieved")
        .isEqualTo("custom foo");
  }

}
