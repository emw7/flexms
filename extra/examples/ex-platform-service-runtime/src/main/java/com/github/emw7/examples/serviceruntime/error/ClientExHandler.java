package com.github.emw7.examples.serviceruntime.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import com.github.emw7.platform.service.runtime.error.handler.AbstractClientExceptionHandler;
import com.github.emw7.platform.service.runtime.error.handler.AbstractServerExceptionHandler;
import com.github.emw7.platform.service.runtime.error.handler.BasicClientExceptionHandler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * An example of client exception handler implementation.
 * <p>
 * <b>Note</b>: it extends {@link AbstractServerExceptionHandler} and the {@code handle} method
 * delegates creation of (error) response to {@link AbstractServerExceptionHandler#buildRequestErrorResponse(RequestErrorException)}
 */
@Component
public class ClientExHandler extends BasicClientExceptionHandler {

  private final ObjectMapper objectMapper;

  private ClientExHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
    this.objectMapper= objectMapper;
  }

  /**
   * Implementation could be simplified in {@code return buildRequestErrorResponse(error);} but
   * it has been added some more code only of fun.
   * <p>
   * {@code handle} is an arbitrary name.
   */
  public final RequestErrorResponse handle (@NonNull final ClientRequestErrorException error) {
    final RequestErrorResponse errorResponse= super.handle(error);
    try {
      final String json= objectMapper.writeValueAsString(errorResponse);
      System.err.printf("CLIENT request error: %s%n", json);
      return errorResponse;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
