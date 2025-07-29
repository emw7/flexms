package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;

/**
 * Basic server exception handler from which application exception handler must inherit.
 * <p>
 * A server exception handler must implement its own public methods that simple returns value of 
 * {@link #handle(ServerRequestErrorException)}.
 * For example, for errors managed with `@ControllerAdvice` in the REST context, this can be an 
 * implementation:
 * <pre>
 * @ExceptionHandler(ServerRequestErrorException.class)
 * public ResponseEntity<RequestErrorResponse> serverRequestException(
 *     @NonNull final ServerRequestErrorException e) {
 *   final RequestErrorResponse requestErrorResponse = handle(e);
 *   return ResponseEntity.status(requestErrorResponse.status()).body(requestErrorResponse);
 * }
 * </pre>
 */
public non-sealed class BasicServerExceptionHandler extends AbstractServerExceptionHandler {

  public BasicServerExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  public RequestErrorResponse handle (@NonNull final ServerRequestErrorException error) {
    return buildRequestErrorResponse(error);
  }

}
