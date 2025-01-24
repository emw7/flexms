package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;

public class BasicClientExceptionHandler extends AbstractClientExceptionHandler {

  private BasicClientExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  public RequestErrorResponse handle (@NonNull final ClientRequestErrorException error) {
    return buildRequestErrorResponse(error);
  }

}
