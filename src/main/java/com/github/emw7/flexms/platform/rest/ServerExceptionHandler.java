package com.github.emw7.flexms.platform.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.emw7.flexms.platform.error.core.ClientRequestErrorException;
import com.github.emw7.flexms.platform.error.core.Constants;
import com.github.emw7.flexms.platform.error.core.RequestErrorResponse;
import com.github.emw7.flexms.platform.error.core.ServerRequestErrorException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
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
public class ServerExceptionHandler {

  //region Constants
//  /**
//   * The list of client error types.
//   * <p>
//   * These are annotations that categorizes the error.<br/> Each new annotation that adds an error
//   * category must be added here.
//   */
//  private static final List<Class<? extends Annotation>> REQUEST_ERROR_TYPES = List.of(
//      BadRequest.class, NotFound.class);
  //endregion Constants

  //region Public types

  //endregion Public type

  //region Private properties
  /**
   * Used to convert from and to json.
   */
  private final ObjectReader objectReader;
  //endregion Private properties

  //region Constructors
  public ServerExceptionHandler(@NonNull final ObjectMapper objectMapper) {
    this.objectReader = objectMapper.reader();
  }
  //endregion Constructors

  //region API

  /**
   * Returns the error response built from the specified {@link ClientRequestErrorException}.
   *
   * @param e the client error to manage
   * @return the error response
   */
  @ExceptionHandler(ServerRequestErrorException.class)
  public ResponseEntity<RequestErrorResponse> serverRequestException(
      @NonNull final ServerRequestErrorException e) {
    final RequestErrorResponse clientRequestError = new RequestErrorResponse(
        ZonedDateTime.now(ZoneOffset.UTC), e.getType(), Constants.SERVER_ERROR_CODE, e.getCode(),
        Constants.SERVER_ERROR_LABEL, Map.of(), e.getErrors());

    return ResponseEntity.status(clientRequestError.httpStatus()).body(clientRequestError);
  }
  //endregion API

  //region Private methods
  //endregion Private methods

}
