package com.github.emw7.platform.error;

import com.github.emw7.platform.error.RequestErrorException.Error;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;


public class RequestErrorExceptionTests {

//  //region API
//  //region Single error
//  @Test
//  public void givenNullMessageAndEmptyParams_whenGetMessage_thenMessageFromLabel() {
//    final List<Error> errors = List.of(new Error(null, "test.error-a", Map.of()));
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
