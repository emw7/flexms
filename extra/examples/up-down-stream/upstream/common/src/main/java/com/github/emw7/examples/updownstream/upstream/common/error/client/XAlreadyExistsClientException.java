package com.github.emw7.examples.updownstream.upstream.common.error.client;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.AlreadyExistsClientException;
import org.springframework.lang.NonNull;

public final class XAlreadyExistsClientException extends AlreadyExistsClientException {

  public XAlreadyExistsClientException(@NonNull final String sensorCode) {
    super(new Id("0"), "sensor", sensorCode, null);
  }

}
