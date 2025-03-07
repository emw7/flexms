package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.BadPropertiesClientException.BadPropertiesResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.BadPropertiesClientException.BadPropertyError;
import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper.CannotMapException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BadPropertiesResponseToExceptionMapperTest extends
    RequestErrorExceptionTestSupport {

  @Test
  public void x() throws CannotMapException {
    final BadPropertiesResponseToExceptionMapper mapper = BadPropertiesClientException.mapper();

    final String ref= String.format("%s-%s",BadPropertiesClientException.CODE.toString(), new Id("0"));
    final ZonedDateTime dateTime = ZonedDateTime.now();
    final BadPropertyError min = BadPropertiesClientException.min("min", 0, 1);
    final BadPropertyError mustBeNotNull = BadPropertiesClientException.mustBeNotNull(
        "mustBeNotNull");

    final RequestErrorResponse requestErrorResponse =
        new RequestErrorResponse(dateTime, ClientRequestErrorException.TYPE, 0,
            ref, "tis", "sid", "message", "label",
            List.of(min.map(), mustBeNotNull.map()));

    final BadPropertiesClientException e = mapper.map(requestErrorResponse);

    assertRef(e, ref);
    assertErrors(e,List.of(min.map(), mustBeNotNull.map()));
  }

}
