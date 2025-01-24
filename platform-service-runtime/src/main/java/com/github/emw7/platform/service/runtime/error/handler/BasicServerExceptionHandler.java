package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;

public class BasicServerExceptionHandler extends AbstractServerExceptionHandler {

  private BasicServerExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  public RequestErrorResponse handle (@NonNull final ServerRequestErrorException error) {
    return buildRequestErrorResponse(error);
  }

}
