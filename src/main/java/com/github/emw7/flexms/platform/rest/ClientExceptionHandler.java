package com.github.emw7.flexms.platform.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.emw7.flexms.platform.error.core.ClientRequestErrorException;
import com.github.emw7.flexms.platform.error.core.Constants;
import com.github.emw7.flexms.platform.error.core.RequestError;
import com.github.emw7.flexms.platform.error.core.RequestErrorException.Error;
import com.github.emw7.flexms.platform.error.core.RequestErrorResponse;
import com.github.emw7.flexms.platform.error.core.category.BadRequest;
import com.github.emw7.flexms.platform.error.core.category.NotFound;
import java.lang.annotation.Annotation;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// no need to create bean manually... spring can find @ControllerAdvice autonomously:
//  from log with --debug:
//  [CUT] .m.m.a.ExceptionHandlerExceptionResolver : ControllerAdvice beans: 2 @ExceptionHandler, 1 ResponseBodyAdvice
/**
 * Prepare answer for a {@link ClientRequestErrorException}.
 * <p>
 * Answer is {@link RequestErrorResponse}.
 */
@ControllerAdvice
public class ClientExceptionHandler {

  //region Constants
  /**
   * The list of client error types.
   * <p>
   * These are annotations that categorizes the error.<br/> Each new annotation that adds an error
   * category must be added here.
   */
  private static final List<Class<? extends Annotation>> REQUEST_ERROR_TYPES = List.of(
      BadRequest.class, NotFound.class);
  //endregion Constants

  //region Private properties
  /**
   * Used to convert from and to json.
   */
  private final ObjectReader objectReader;
  //endregion Private properties

  //region Constructors
  public ClientExceptionHandler(@NonNull final ObjectMapper objectMapper) {
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
  @ExceptionHandler(ClientRequestErrorException.class)
  public ResponseEntity<RequestErrorResponse> clientRequestException(
      @NonNull final ClientRequestErrorException e) {
    // retrieve the RequestError annotation (could be null in case of programming error).
    final RequestError requestError = AnnotationUtils.findAnnotation(e.getClass(),
        RequestError.class);
    // retrieve the error type annotation properties.
    final Map<String, Object> annotationProperties = annotationProperties(e);

    // retrieve information to be put in the response (body).
    final int httpStatus = httpStatus(requestError);
    final String label = label(annotationProperties, requestError);
    final Map<String, Object> params = params(annotationProperties);

    final RequestErrorResponse requestErrorResponse = new RequestErrorResponse(
        ZonedDateTime.now(ZoneOffset.UTC), e.getType(), httpStatus, e.getCode(), label, params,
        e.getErrors());

    return ResponseEntity.status(requestErrorResponse.httpStatus()).body(requestErrorResponse);
  }
  //endregion API

  //region Private methods
  private int httpStatus(@Nullable RequestError requestError) {
    if (requestError == null) {
      // requestError == null => programming error, but try to manage the case.
      return Constants.DEFAULT_CLIENT_ERROR_CODE;
    } else {
      return requestError.errorCode();
    }
  }

  private @NonNull String label(@NonNull final Map<String, Object> annotationProperties,
      @Nullable final RequestError requestError) {
    final String label = (String) annotationProperties.get("label");
    if (StringUtils.hasLength(label)) {
      return label;
    } else if (requestError != null) {
      return requestError.label();
    } else {
      return Constants.DEFAULT_ERROR_LABEL;
    }
  }

  private @NonNull Map<String, Object> params(
      @NonNull final Map<String, Object> annotationProperties) {
    final String params = (String) annotationProperties.get("params");
    try {
      return objectReader.readValue(Optional.ofNullable(params).orElse(""));
    } catch (JsonProcessingException e) {
      // TODO log error
      return Map.of();
    }
  }

  // requestError == null => programming error, but try to manage the case.
  private @NonNull Map<String, Object> annotationProperties(
      @NonNull final ClientRequestErrorException e) {
    for (Class<? extends Annotation> annotationType : REQUEST_ERROR_TYPES) {
      final Annotation requestErrorAnnotation = findAnnotation(e.getClass(), annotationType);
      if (requestErrorAnnotation != null) {
        return AnnotationUtils.getAnnotationAttributes(requestErrorAnnotation);
      }
    }
    // no request error annotation found.
    return new HashMap<>();
  }

  private Annotation findAnnotation(@Nullable final Class<?> clazz,
      @Nullable final Class<? extends Annotation> annotationType) {
    if (clazz == null || annotationType == null) {
      return null;
    }
    // else...
    @SuppressWarnings("UnnecessaryLocalVariable") final Annotation annotation = AnnotationUtils.findAnnotation(
        clazz, annotationType);
    return annotation;
  }
  //endregion Private methods

}
