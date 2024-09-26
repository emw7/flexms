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
   DESIGN

   IMPORTANT: In this comment is described a general mean of testing containers that have static
   access, that is, they are utility classes
   See [README :: Containers](https://github.com/emw7/flexms/blob/5-transform-flexms-service-to-a-frontend-service/README.md#Containers)

   Why do tests use the following construct?
   try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
       TranslatorContainer.class)) {

   Because I18nEnabledException (not being managed by Spring) uses TranslatorContainer to retrieve
   the translator bean, and TranslatorContainer has static access, that is, it is an utility class.

   Initially, tests were implemented as follow (using testA_WithoutCause as example):
    1 @Mock private MessageSource messageSource;

    2 @BeforeEach public void init() {
    3  final Translator translator = new MessageSourceTranslator(messageSource, new I18nLabelPrefixes(null));
    4  new TranslatorContainer(translator);
    5 }
   
    6 @Test public void testA_WithoutCause() {
    7 when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
    8      "here we are: {errorReason}");
    9 I18nEnabledException sut = new I18nEnabledException("here.we.are", null, null, false, false) {};
   10 assertThat(sut.getMessage()).isEqualTo("here we are: unknown");
   11  verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));
   12 }

   That worked till not changed TranslatorContainer for not setting the translator static property if already
   not null: after such a change, only the test run as first was successfully while the others failed.
   What was happening was that messageSource mock at every test got a different instance that was injected into
   translator (row 3) and in turn, translator was injected into TranslatorContainer (row 4). That
   worked before the changes because TranslatorContainer constructor overrode the translator static property, but
   after the changes the translator static property was not overridden and so while all was fine for the first test,
   the next ones, even if stubbed the new messageSource mock (rows 7, 8) (referenced by the new translator (row 3),
   the I18nEnabledException uses the old one that was referenced by the old translator that was
   actually returned by TranslatorContainer.

   The solution has been to mock the TranslatorContainer#getTranslator() method to return the
   new translator instance so the I18nEnabledException uses it and consequentially triggering the
   new messageSource mock instance with the fresh stubs.

   References:
   - https://www.baeldung.com/mockito-mock-static-methods#mocking-a-no-argument-static-method
   - https://dzone.com/articles/demystifying-static-mocking-with-mockito
 */
@ExtendWith(MockitoExtension.class)
public class I18nEnabledExceptionTest {

  @Mock
  private MessageSource messageSource;

  private Translator translator;

  @BeforeEach
  public void init() {
    translator = new MessageSourceTranslator(messageSource, new I18nLabelPrefixes(null));
  }

  //region A: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
  @Test
  public void testA_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);

      I18nEnabledException sut = new I18nEnabledException("here.we.are", null, null, false, false) {
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", null,
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", Map.of("who", "we are"),
          null, false, false) {
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", Map.of("who", "we are"),
          new NullPointerException(), false, false) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }
  //endregion A: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)

  //region B: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause)
  @Test
  public void testB_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledException sut = new I18nEnabledException("here.we.are", null, null) {
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", null,
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", Map.of("who", "we are"),
          null) {
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", Map.of("who", "we are"),
          new NullPointerException()) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: null");

      verify(messageSource, times(1)).getMessage(eq("here.we.are"), isNull(), any(Locale.class));
    }
  }
  //endregion B: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params, final Throwable cause)

  //region C: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params)
  @Test
  public void testC_WithoutCause() {
    when(messageSource.getMessage(anyString(), isNull(), any(Locale.class))).thenReturn("unknown",
        "here we are: {errorReason}");

    try (MockedStatic<TranslatorContainer> translatorContainer = Mockito.mockStatic(
        TranslatorContainer.class)) {
      translatorContainer.when(TranslatorContainer::getTranslator).thenReturn(translator);
      I18nEnabledException sut = new I18nEnabledException("here.we.are", null) {
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
      I18nEnabledException sut = new I18nEnabledException("here.we.are", Map.of("who", "we are")) {
      };

      assertThat(sut.getMessage()).isEqualTo("here we are: unknown");

      verify(messageSource, times(2)).getMessage(anyString(), isNull(), any(Locale.class));
    }
  }
  //endregion C: I18nEnabledException(@NonNull final String label, @Nullable final Map<String, Object> params)

}
