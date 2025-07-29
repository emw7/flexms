package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;

/**
 * Basic client exception handler from which application exception handler must inherit.
 * <p>
 * A client exception handler must implement its own public methods that simple returns value of
 * {@link #handle(ClientRequestErrorException)}.
 * For example, for errors managed with `@ControllerAdvice` in the REST context, this can be an
 * implementation:
 * <pre>
 * @ExceptionHandler(ClientRequestErrorException.class)
 * public ResponseEntity<RequestErrorResponse> clientRequestException(
 *     @NonNull final ClientRequestErrorException e) {
 *   final RequestErrorResponse requestErrorResponse = handle(e);
 *   return ResponseEntity.status(requestErrorResponse.status()).body(requestErrorResponse);
 * }
 * </pre>
 */
public non-sealed class BasicClientExceptionHandler extends AbstractClientExceptionHandler {

  public BasicClientExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  public RequestErrorResponse handle (@NonNull final ClientRequestErrorException error) {
    return buildRequestErrorResponse(error);
  }

}
