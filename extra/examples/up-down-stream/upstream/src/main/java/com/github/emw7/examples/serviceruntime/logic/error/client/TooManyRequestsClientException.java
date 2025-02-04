package com.github.emw7.examples.serviceruntime.logic.error.client;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import java.util.List;
import org.springframework.lang.NonNull;

@TooManyRequestsClientError
public final class TooManyRequestsClientException extends ClientRequestErrorException {

  public static final Code CODE= new Code("KCYVD");

  public TooManyRequestsClientException(@NonNull final Id id) {
    super(CODE, id, (List<Error>) null);
  }

}
