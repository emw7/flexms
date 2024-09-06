package com.github.emw7.platform.service.runtime.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import com.github.emw7.platform.error.ClientRequestErrorException;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.runtime.error.handler.AbstractClientExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Prepare answer for a {@link ClientRequestErrorException}.
 * <p>
 * Answer is {@link RequestErrorResponse}.
 */
@ControllerAdvice
public class ClientExceptionHandler extends AbstractClientExceptionHandler {

  //region Constructors

  public ClientExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }
  //endregion Constructors

  //region API

  /**
   * Returns the error response built from the specified {@link ClientRequestErrorException}.
   *
   * @param e the client error to manage
   * @return the error response
   */
  @ExceptionHandler(ClientRequestErrorException.class)
  public ResponseEntity<RequestErrorResponse> clientRequestException(
      @NonNull final ClientRequestErrorException e) {
    final RequestErrorResponse requestErrorResponse = buildRequestErrorResponse(e);
    return ResponseEntity.status(requestErrorResponse.status()).body(requestErrorResponse);
  }
  //endregion API

}
