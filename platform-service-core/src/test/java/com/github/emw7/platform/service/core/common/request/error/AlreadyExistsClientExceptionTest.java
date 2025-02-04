package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.category.AlreadyExists;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AlreadyExistsClientExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testAlreadyExistsClientException() {
    final AlreadyExistsClientException e = new AlreadyExistsClientException(new Id("0555T"),
        "testAlreadyExistsClientException", 0, Map.of("p", false)) {
    };

    assertRef(e, "D4FAA-0555T");
    assertType(e, "CLIENT");
    assertError(e,
        "com.github.emw7.platform.i18n.error.request.client.already-exists.testAlreadyExistsClientException",
        Map.of("p", false, "resourceName", "testAlreadyExistsClientException", "resourceId", 0));

    assertAnnotation(e, AlreadyExists.class,
        "com.github.emw7.platform.i18n.error.request.client.already-exists", 409);
//    Assertions.assertThat(findAnnotation(e.getClass(), AlreadyExists.class))
//        .as("test error has right request error extension annotation")
//        .isNotNull();
//
//    Map<String, Object> annotationAttributes= annotationProperties(e);
//    Assertions.assertThat(annotationAttributes)
//        .hasEntrySatisfying("label",
//            new Condition<>(
//                "com.github.emw7.platform.i18n.error.request.client.already-exists"::equals,""))
//        .hasEntrySatisfying("errorCode",             new Condition<>(
//            v -> (v instanceof Integer n) && n == 409,""));
  }

}