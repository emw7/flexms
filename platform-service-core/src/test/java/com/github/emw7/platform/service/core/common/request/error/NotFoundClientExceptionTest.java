package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.category.AlreadyExists;
import com.github.emw7.platform.service.core.common.request.error.category.BadRequest;
import com.github.emw7.platform.service.core.common.request.error.category.NotFound;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotFoundClientExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testNotFoundClientException() {
    final NotFoundClientException e = new NotFoundClientException(new Id("FV3KB"),
        "testNotFoundClientException", 0) {
    };

    assertRef(e, "RY19P-FV3KB");
    assertType(e, "CLIENT");
    assertError(e,
        "com.github.emw7.platform.i18n.error.request.client.not-found.testNotFoundClientException",
        Map.of( "resourceName", "testNotFoundClientException", "resourceId", 0));

    assertAnnotation(e, NotFound.class,
        "com.github.emw7.platform.i18n.error.request.client.not-found", 404);

//    Assertions.assertThat(findAnnotation(e.getClass(), NotFound.class))
//        .as("test error has right request error extension annotation")
//        .isNotNull();
//
//    Map<String, Object> annotationAttributes= annotationProperties(e);
//    Assertions.assertThat(annotationAttributes)
//        .hasEntrySatisfying("label",
//            new Condition<>(
//                "com.github.emw7.platform.i18n.error.request.client.not-found"::equals,""))
//        .hasEntrySatisfying("errorCode",             new Condition<>(
//            v -> (v instanceof Integer n) && n == 404,""));
  }

}