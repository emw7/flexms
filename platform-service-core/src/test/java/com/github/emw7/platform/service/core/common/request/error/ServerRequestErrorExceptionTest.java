package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ServerRequestErrorExceptionTest extends RequestErrorExceptionTestSupport {

  @Test
  public void testServerRequestErrorException() {
    final ServerRequestErrorException e = new ServerRequestErrorException(new Code("AB123"),
        new Id("12345"), new Error("test.error.ServerRequestErrorException", Map.of("s", "ss", "n", 9, "b", false))) {
    };

    assertRef(e, "AB123-12345");
    assertType(e, "SERVER");
    assertError(e, "test.error.ServerRequestErrorException", Map.of("s", "ss", "n", 9, "b", false));
  }

}