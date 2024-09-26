package com.github.emw7.platform.i18n;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@ExtendWith(MockitoExtension.class)
class CompositeMessageSourceTest {

  @Mock
  private MessageSource appMessageSource;

  @Mock
  MessageSource emw7PlatformMessageSource;

  //region code is null
  @Test
  void codeIsNullNoDefault() {
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    Assertions.assertThatThrownBy(() -> sut.getMessage(null, null, null)).isInstanceOf(
        NoSuchMessageException.class);
    verify(emw7PlatformMessageSource, never()).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void codeIsNullDefault() {
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage(null, null, "the default code", null);
    Assertions.assertThat(message).as("codeIsNullDefault returns null").isNull();
    verify(emw7PlatformMessageSource, never()).getMessage(eq("code"), isNull(), any(Locale.class));
}
  //endregion code is null

  //region default appMessageSource

  // tests in this region use test messages resource bundle.

  //region no default message
  @Test
  void defaultAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources() {
    Mockito.doReturn("the code").when(emw7PlatformMessageSource)
        .getMessage(eq("code"), isNull(), any(Locale.class));

    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, null);
    Assertions.assertThat(message).as("defaultAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources returns message of non app message sources").isEqualTo("the code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void defaultAppMessageSourceNoDefaultMessageCodeExistsInAppMessageSource() {
    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("plain.test.label.foo", null, null);
    Assertions.assertThat(message).as("defaultAppMessageSourceNoDefaultMessageCodeExistsInAppMessageSource returns message of app message source").isEqualTo("plain foo");
    verify(emw7PlatformMessageSource, never()).getMessage(eq("plain.test.label.foo"), isNull(), any(Locale.class));
  }

  @Test
  void defaultAppMessageSourceNoDefaultMessageCodeDoesNotExistAnywhere() {
    Mockito.doThrow(NoSuchMessageException.class).when(emw7PlatformMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    Assertions.assertThatThrownBy(() -> sut.getMessage("code", null, null)).as("defaultAppMessageSourceNoDefaultMessageCodeDoesNotExistAnywhere throws exception").isInstanceOf(
        NoSuchMessageException.class);
    verify(emw7PlatformMessageSource,times(1)).getMessage(anyString(), isNull(), any(Locale.class));
  }
  //endregion no default message

  //region default message
  @Test
  void defaultAppMessageSourceDefaultMessageCodeExistsOnlyInNonAppMessageSources() {
    Mockito.doReturn("the code").when(emw7PlatformMessageSource)
        .getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, "the default code", null);
    Assertions.assertThat(message).as("defaultAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources returns message of non app message sources").isEqualTo("the code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void defaultAppMessageSourceDefaultMessageCodeExistsInAppMessageSource() {
    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("plain.test.label.foo", null, "the default code", null);
    Assertions.assertThat(message).as("defaultAppMessageSourceNoDefaultMessageCodeExistsInAppMessageSource returns message of app message source").isEqualTo("plain foo");
    verify(emw7PlatformMessageSource, never()).getMessage(eq("plain.test.label.foo"), isNull(), any(Locale.class));
  }

  @Test
  void defaultAppMessageSourceDefaultMessageCodeDoesNotExistAnywhere() {
    final Map<String, MessageSource> messageSources = Map.of("emw7PlatformMessageSource",
        emw7PlatformMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, "the default code",  null);
    Assertions.assertThat(message).as("defaultAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources returns the default message").isEqualTo("the default code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }
  //endregion default message
  //endregion default appMessageSource

  //region custom appMessageSource
  //region no default message
  @Test
  void customAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources() {
    Mockito.doReturn("the code").when(emw7PlatformMessageSource)
        .getMessage(eq("code"), isNull(), any(Locale.class));
    Mockito.doThrow(NoSuchMessageException.class).when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));

    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, null);
    Assertions.assertThat(message).as("customAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources returns message of non app message sources").isEqualTo("the code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void customAppMessageSourceNoDefaultMessageCodeExistsInAppMessageSource() {
    Mockito.doReturn("the custom code").when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, null);
    Assertions.assertThat(message).as("customAppMessageSourceNoDefaultMessageCodeExistsInAppMessageSource returns message of app message source").isEqualTo("the custom code");
    verify(emw7PlatformMessageSource, never()).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void customAppMessageSourceNoDefaultMessageCodeDoesNotExistAnywhere() {
    Mockito.doThrow(NoSuchMessageException.class).when(emw7PlatformMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    Mockito.doThrow(NoSuchMessageException.class).when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    Assertions.assertThatThrownBy(() -> sut.getMessage("code", null, null)).as("customAppMessageSourceNoDefaultMessageCodeDoesNotExistAnywhere throws exception").isInstanceOf(
        NoSuchMessageException.class);
    verify(emw7PlatformMessageSource,times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource,times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }
  //endregion no default message

  //region default message
  @Test
  void customAppMessageSourceDefaultMessageCodeExistsOnlyInNonAppMessageSources() {
    Mockito.doReturn("the code").when(emw7PlatformMessageSource)
        .getMessage(eq("code"), isNull(), any(Locale.class));
    Mockito.doThrow(NoSuchMessageException.class).when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, "the default code", null);
    Assertions.assertThat(message).as("customAppMessageSourceDefaultMessageCodeExistsOnlyInNonAppMessageSources returns message of non app message sources").isEqualTo("the code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void customAppMessageSourceDefaultMessageCodeExistsInAppMessageSource() {
    Mockito.doReturn("the custom code").when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, "the default code", null);
    Assertions.assertThat(message).as("customAppMessageSourceDefaultMessageCodeExistsInAppMessageSource returns message of app message source").isEqualTo("the custom code");
    verify(emw7PlatformMessageSource, never()).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
  }

  @Test
  void customAppMessageSourceDefaultMessageCodeDoesNotExistAnywhere() {
    Mockito.doThrow(NoSuchMessageException.class).when(emw7PlatformMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    Mockito.doThrow(NoSuchMessageException.class).when(appMessageSource).getMessage(eq("code"), isNull(), any(Locale.class));
    Mockito.doReturn("the default code").when(appMessageSource).getMessage(eq("code"), isNull(), eq("the default code"), any(Locale.class));
    final Map<String, MessageSource> messageSources = Map.of(
        "emw7PlatformMessageSource", emw7PlatformMessageSource,
        "appMessageSource", appMessageSource);
    final CompositeMessageSource sut = new CompositeMessageSource(messageSources);
    final String message= sut.getMessage("code", null, "the default code",  null);
    Assertions.assertThat(message).as("customAppMessageSourceNoDefaultMessageCodeExistsOnlyInNonAppMessageSources returns the default message").isEqualTo("the default code");
    verify(emw7PlatformMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), any(Locale.class));
    verify(appMessageSource, times(1)).getMessage(eq("code"), isNull(), eq("the default code"), any(Locale.class));
  }
  //endregion default message
  //endregion custom appMessageSource
}