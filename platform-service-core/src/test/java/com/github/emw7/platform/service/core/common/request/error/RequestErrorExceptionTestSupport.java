package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.core.collection.ContainsExactlyInAnyOrder;
import com.github.emw7.platform.core.function.Equality;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// This is disabled as RequestErrorException is a sealed class and it does not make sense testing it,
//  while permitted classes should be tested.
public abstract class RequestErrorExceptionTestSupport {

  protected void assertRef(@NonNull final RequestErrorException e,
      @NonNull final String expectedRef) {
    Assertions.assertThat(e.getRef()).as("test error reference (ref)").isEqualTo(expectedRef);
  }

  protected void assertType(@NonNull final RequestErrorException e,
      @NonNull final String expectedType) {
    Assertions.assertThat(e.getType()).as("test error type").isEqualTo(expectedType);
  }

  // Assert exception with a single error.
  // YES... the error message is not tested!
  protected void assertError(@NonNull final RequestErrorException e,
      @NonNull final String expectedLabel, @NonNull final Map<String, Object> expectedParams) {
    // getFirst() throws an exception is there are no elements.
    final Error actualError = e.getErrors().getFirst();

    Assertions.assertThat(actualError.label()).as("test error label").isEqualTo(expectedLabel);

    // Cannot be null, see RequestErrorException.Error#Error constructor.
    final Map<String, Object> actualParams = actualError.params();

    Assertions.assertThat(expectedParams).as("test error parameters")
        .containsExactlyInAnyOrderEntriesOf(actualParams);
  }

  // Assert exception with a multiple error.
  // YES... the error message is not tested!
  protected void assertErrors(@NonNull final RequestErrorException e,
      @Nullable final List<Error> errors) {

    class ErrorEquality implements Equality<Error> {

      @Override
      public boolean test(final Error error1, final Error error2) {
        if (error1 == null) {
          return error2 == null;
        } else if (error2 == null) {
          return false;
        }
        // error1 and error2 are not null.

        // Test label.
        if (!error1.label().equals(error2.label())) {
          return false;
        }

        // Test params
        if (error1.params() == null) {
          return error2.params() == null;
        } else if (error2.params() == null) {
          return false;
        } else {
          // error1.params() and error2.params() are not null.
          // This equality test does not respect the order... but who knows the order of map's entries?
          return error1.params().equals(error2.params());
        }
      }
    }

    if (errors == null || errors.isEmpty()) {
      Assertions.assertThat(e.getErrors()).as("test errors list is empty").isEmpty();
      return;
    }
    // else...
    //final ErrorEq eeq = new ErrorEq();
    final ContainsExactlyInAnyOrder<Error> x = new ContainsExactlyInAnyOrder<>(e.getErrors(),
        new ErrorEquality());
    Assertions.assertThat(x.test(errors)).as("test errors list are the same is any order").isTrue();

    //Assertions.assertThat(e.getErrors()).as("test errors list").containsExactlyInAnyOrderElementsOf(errors);
  }

  // requestError == null => programming error, but try to manage the case.
  protected final @NonNull Map<String, Object> annotationProperties(
      @NonNull final RequestErrorException e) {

    final Annotation requestErrorAnnotation = findAnnotation(e.getClass(), RequestError.class);
    return (requestErrorAnnotation != null) ? AnnotationUtils.getAnnotationAttributes(
        requestErrorAnnotation) : Map.of();
  }

  protected Annotation findAnnotation(@Nullable final Class<?> clazz,
      @Nullable final Class<? extends Annotation> annotationType) {
    if (clazz == null || annotationType == null) {
      return null;
    }
    // else...
    @SuppressWarnings("UnnecessaryLocalVariable") final Annotation annotation = AnnotationUtils.findAnnotation(
        clazz, annotationType);
    return annotation;
  }

  protected void assertAnnotation(@NonNull final RequestErrorException e,
      @NonNull final Class<? extends Annotation> annotationClazz, @NonNull final String i18nLabel,
      final int errorCode) {
    Assertions.assertThat(findAnnotation(e.getClass(), annotationClazz))
        .as("test error has right request error extension annotation").isNotNull();

    Map<String, Object> annotationAttributes = annotationProperties(e);
    Assertions.assertThat(annotationAttributes).hasEntrySatisfying("label",
            new Condition<>(i18nLabel::equals, String.format("error label must be %s", i18nLabel)))
        .hasEntrySatisfying("errorCode",
            new Condition<>(v -> (v instanceof Integer n) && n == errorCode,
                String.format("error code must be %d", errorCode)));
  }

  //  //region API
//  //region Single error
//  @Test
//  public void givenNullMessageAndEmptyParams_whenGetMessage_thenMessageFromLabel() {
//    final List<Error> errors = List.of(new Error( "test.error-a", Map.of()));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "01234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message")
//        .isEqualTo("0-01234 @test.error-a::{}");
//  }
//
//  @Test
//  public void givenNullMessageAndParams_whenGetMessage_thenMessageFromLabelWithParams() {
//    final List<Error> errors = List.of(
//        new Error(null, "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "01234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message")
//        .isEqualTo("0-01234 @test.error-b::{a=1, b=bi, c=1.2}");
//  }
//
//  @Test
//  public void givenMessageAndEmptyParams_whenGetMessage_thenMessageFromMessage() {
//    final List<Error> errors = List.of(new Error("Test error", "test.error-a", Map.of()));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "1234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234 @Test error");
//  }
//
//  @Test
//  public void givenMessageAndParams_whenGetMessage_thenMessageFromMessage() {
//    final List<Error> errors = List.of(
//        new Error("Test error", "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "1234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234 @Test error");
//  }
//
//  //endregion Single error
//
//  //region Multiple errors
//  @Test
//  public void givenNullMessageAndEmptyParams_whenGetMessage_thenMessageFromLabelX() {
//    final List<Error> errors = List.of(new Error(null, "test.error-a", Map.of()),
//        new Error("Test error", "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "01234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234");
//  }
//
//  @Test
//  public void givenNullMessageAndParams_whenGetMessage_thenMessageFromLabelWithParamsX() {
//    final List<Error> errors = List.of(new Error(null, "test.error-a", Map.of()),
//        new Error("Test error", "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "01234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234");
//  }
//
//  @Test
//  public void givenMessageAndEmptyParams_whenGetMessage_thenMessageFromMessageX() {
//    final List<Error> errors = List.of(new Error(null, "test.error-a", Map.of()),
//        new Error("Test error", "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "1234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234");
//  }
//
//  @Test
//  public void givenMessageAndParams_whenGetMessage_thenMessageFromMessageX() {
//    final List<Error> errors = List.of(new Error(null, "test.error-a", Map.of()),
//        new Error("Test error", "test.error-b", Map.of("a", 1, "b", "bi", "c", 1.2F)));
//
//    final RequestErrorException e = new RequestErrorException("0", "TEST", "1234", errors) {
//    };
//
//    assertAPI(e, "0", "TEST", "1234", errors);
//
//    Assertions.assertThat(e.getMessage()).as("assert message").isEqualTo("0-01234");
//  }
//  //endregion Multiple errors
//  //endregion API
//
//  //region Private methods
//  private String buildCode0Padded(@NonNull final String code) {
//    return StringUtils.leftPad(code, 5, "0");
//  }
//
//  private void assertAPI(@NonNull final RequestErrorException e, @NonNull final String type,
//      @NonNull final String typeString, @NonNull final String code, List<Error> errors) {
//    final String code0Padded = buildCode0Padded(code);
//    Assertions.assertThat(e.getType()).as("assert type").isEqualTo(type);
//    Assertions.assertThat(e.getTypeString()).as("assert typeString").isEqualTo(typeString);
//    Assertions.assertThat(e.getCode()).as("assert code").isEqualTo(type + '-' + code0Padded);
//    Assertions.assertThat(e.getErrors()).containsExactlyElementsOf(errors);
//  }
//  //endregion Private methods

}
