package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ClientRequestErrorExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testClientRequestErrorException() {
    final ClientRequestErrorException e = new ClientRequestErrorException(new Code("H5ACF"),
        new Id("C1ZZ5"),
        List.of(new Error("test.error.ClientRequestErrorException.a", Map.of("a", true)),
            new Error("test.error.ClientRequestErrorException.b", Map.of("b", false)))) {
    };

    assertRef(e, "H5ACF-C1ZZ5");
    assertType(e, "CLIENT");
    assertErrors(e,
        List.of(new Error("test.error.ClientRequestErrorException.a", Map.of("a", true)),
            new Error("test.error.ClientRequestErrorException.b", Map.of("b", false))));
  }

}