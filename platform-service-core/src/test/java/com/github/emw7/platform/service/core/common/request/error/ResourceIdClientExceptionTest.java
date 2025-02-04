package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ResourceIdClientExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testResourceIdClientException() {
    final ResourceIdClientException e = new ResourceIdClientException(new Code("XABS3"),
        new Id("QZ8RL"), "test.error.ResourceIdClientException", "testResourceIdClientException", 1,
        null) {
    };

    assertRef(e, "XABS3-QZ8RL");
    assertType(e, "CLIENT");
    assertError(e, "test.error.ResourceIdClientException",
        Map.of("resourceName", "testResourceIdClientException", "resourceId", 1));
  }

}