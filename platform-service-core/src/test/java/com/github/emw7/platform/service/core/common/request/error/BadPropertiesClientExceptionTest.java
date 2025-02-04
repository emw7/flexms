package com.github.emw7.platform.service.core.common.request.error;

import static org.junit.jupiter.api.Assertions.*;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.category.BadRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BadPropertiesClientExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testBadPropertiesClientException() {
    final BadPropertiesClientException e = new BadPropertiesClientException(null, new Id("Y2F7N"),
        BadPropertiesClientException.min("x", -1, 0));

    assertRef(e, "FBTYV-Y2F7N");
    assertType(e, "CLIENT");
    assertError(e,
        "com.github.emw7.platform.i18n.error.request.client.bad-request.property-violates-min",
        Map.of("property", "x", "val", -1, "min", 0));

    assertAnnotation(e, BadRequest.class,
        "com.github.emw7.platform.i18n.error.request.client.bad-request", 400);

  }
}