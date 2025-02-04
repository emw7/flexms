package com.github.emw7.platform.service.core.common.request.error;

import static org.junit.jupiter.api.Assertions.*;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import com.github.emw7.platform.service.core.common.request.error.category.AlreadyExists;
import com.github.emw7.platform.service.core.common.request.error.category.BadRequest;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;

class BadRequestClientExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testBadRequestClientException() {
    final BadRequestClientException e = new BadRequestClientException(null, new Code("FRE3X"),
        new Id("GKCTV"),
        List.of(new Error("test.error.BadRequestClientException.a", Map.of("a", true)),
            new Error("test.error.BadRequestClientException.b", Map.of("b", false)))) {
    };

    assertRef(e, "FRE3X-GKCTV");
    assertType(e, "CLIENT");
    assertErrors(e,
        List.of(new Error("test.error.BadRequestClientException.a", Map.of("a", true)),
            new Error("test.error.BadRequestClientException.b", Map.of("b", false))));

    assertAnnotation(e, BadRequest.class,
        "com.github.emw7.platform.i18n.error.request.client.bad-request", 400);
//    Assertions.assertThat(findAnnotation(e.getClass(), BadRequest.class))
//        .as("test error has right request error extension annotation")
//        .isNotNull();
//
//    Map<String, Object> annotationAttributes= annotationProperties(e);
//    Assertions.assertThat(annotationAttributes)
//        .hasEntrySatisfying("label",
//            new Condition<>(
//            "com.github.emw7.platform.i18n.error.request.client.bad-request"::equals,""))
//        .hasEntrySatisfying("errorCode",             new Condition<>(
//            v -> (v instanceof Integer n) && n == 400,""));
  }

}
