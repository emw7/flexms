package com.github.emw7.platform.app.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.app.error.handler.AbstractServerExceptionHandler;
import com.github.emw7.platform.app.error.model.RequestErrorResponse;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.i18n.Translator;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Prepare answer for a {@link ServerRequestErrorException}.
 * <p>
 * Answer is {@link RequestErrorResponse}.
 */
@ControllerAdvice
public class ServerExceptionHandler extends AbstractServerExceptionHandler {

  //region Constructors

  public ServerExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }
  //endregion Constructors

  //region API

  /**
   * Returns the error response built from the specified {@link ServerRequestErrorException}.
   *
   * @param e the client error to manage
   * @return the error response
   */
  @ExceptionHandler(ServerRequestErrorException.class)
  public ResponseEntity<RequestErrorResponse> serverRequestException(
      @NonNull final ServerRequestErrorException e) {
    final RequestErrorResponse requestErrorResponse = buildRequestErrorResponse(e);
    return ResponseEntity.status(requestErrorResponse.status()).body(requestErrorResponse);
  }
  //endregion API

}
