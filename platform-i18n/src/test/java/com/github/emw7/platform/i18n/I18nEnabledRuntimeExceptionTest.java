package com.github.emw7.platform.i18n;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

/*
   This is a copy of I18nEnabledExceptionTest with reference to I18nEnabledExceptionTest
   replaced with references to I18nEnabledRuntimeExceptionTest.
 */
@ExtendWith(MockitoExtension.class)
public class I18nEnabledRuntimeExceptionTest {

  @Mock
  private MessageSource messageSource;

  private Translator translator;

  @BeforeEach
  public void init() {
    translator = new MessageSourceTranslator(messageSource, new I18nLabelPrefixes(null));
  }

  //region A: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
  @Test
  public void testA_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);

      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are", null, null,
          false, false) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));

    }
  }

  @Test
  public void testA_WithCause() {
    when(messageSource.getMessage(eq("here.we.are"), isNull(), any(Locale.class))).thenReturn(
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are", null,
          new NullPointerException(), false, false) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }

  @Test
  public void testA_WithArgsWithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here {who}: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are",
          Map.of("who", "we are"), null, false, false) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));
    }
  }

  @Test
  public void testA_WithArgsWithCause() {
    when(messageSource.getMessage(eq("here.we.are"), isNull(), any(Locale.class))).thenReturn(
        "here {who}: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are",
          Map.of("who", "we are"), new NullPointerException(), false, false) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }
  //endregion A: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)

  //region B: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause)
  @Test
  public void testB_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are", null, null) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));

    }
  }

  @Test
  public void testB_WithCause() {
    when(messageSource.getMessage(eq("here.we.are"), isNull(), any(Locale.class))).thenReturn(
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are", null,
          new NullPointerException()) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }

  @Test
  public void testB_WithArgsWithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here {who}: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are",
          Map.of("who", "we are"), null) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));
    }
  }

  @Test
  public void testB_WithArgsWithCause() {
    when(messageSource.getMessage(eq("here.we.are"), isNull(), any(Locale.class))).thenReturn(
        "here {who}: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are",
          Map.of("who", "we are"), new NullPointerException()) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }
  //endregion B: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause)

  //region C: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params)
  @Test
  public void testC_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are", null) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));

    }
  }

  @Test
  public void testC_WithArgsWithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here {who}: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledRuntimeException sut = new I18nEnabledRuntimeException("here.we.are",
          Map.of("who", "we are")) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));
    }
  }
  //endregion C: I18nEnabledRuntimeException(@NonNull final String label, @Nullable final Map<String, Object> params)

}
