package com.github.emw7.platform.app.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.emw7.platform.app.error.model.RequestErrorResponse;
import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.category.AlreadyExists;
import com.github.emw7.platform.error.category.BadRequest;
import com.github.emw7.platform.error.category.NotFound;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.telemetry.tracing.Tracing;
import com.github.emw7.platform.telemetry.tracing.TracingContainer;
import java.lang.annotation.Annotation;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// WARNING TEST: it is not easy to test because uses static/singleton TracingContainer.
public abstract sealed class AbstractExceptionHandler permits AbstractClientExceptionHandler,
    AbstractServerExceptionHandler {

  //region Private static properties
  private static final Logger logger= LoggerFactory.getLogger(AbstractExceptionHandler.class);

  /**
   * The list of client error types.
   * <p>
   * These are annotations that categorize the error.<br/>
   * Each new annotation that adds an error category must be added here.
   */
  private static final List<Class<? extends Annotation>> REQUEST_ERROR_TYPES = List.of(
      BadRequest.class, NotFound.class, AlreadyExists.class);
  //endregion Constants

  //region Private properties
  /**
   * Used to convert from and to json.
   */
  private final ObjectReader objectReader;

  private final Translator translator;
  //endregion Private properties

  //region Constructors
  protected AbstractExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    this.objectReader = objectMapper.reader().forType(Map.class);
    this.translator = translator;
  }
  //endregion Constructors

  //region API

  private final @Nullable RequestError retrieveRequestError(
      @NonNull Class<? extends RequestErrorException> clazz) {
    return AnnotationUtils.findAnnotation(clazz, RequestError.class);
  }

  // requestError == null => programming error, but try to manage the case.
  private final @NonNull Map<String, Object>  annotationProperties(
      @NonNull final RequestErrorException e) {
    for (Class<? extends Annotation> annotationType : REQUEST_ERROR_TYPES) {
      final Annotation requestErrorAnnotation = findAnnotation(e.getClass(), annotationType);
      if (requestErrorAnnotation != null) {
        return AnnotationUtils.getAnnotationAttributes(requestErrorAnnotation);
      }
    }
    // no request error annotation found.
    return new HashMap<>();
  }

  protected int retrieveStatus(@Nullable final RequestError requestError) {
    if (requestError == null) {
      // requestError == null => programming error, but try to manage the case.
      return defaultStatus();
    } else {
      return requestError.errorCode();
    }
  }

  protected @NonNull String label(@NonNull final Map<String, Object> annotationProperties,
      @Nullable final RequestError requestError) {
    final String label = (String) annotationProperties.get("label");
    if (StringUtils.isNotEmpty(label)) {
      return label;
    } else if (requestError != null) {
      return requestError.label();
    } else {
      return Constants.DEFAULT_ERROR_LABEL;
    }
  }

  private final @NonNull Map<String, Object> params(
      @NonNull final Map<String, Object> annotationProperties) {
    final String params = Optional.ofNullable((String)annotationProperties.get("params")).orElse("{}");
    try {
      return objectReader.readValue(params);
    } catch (JsonProcessingException e) {
      logger.warn("[JSON-PROCESSING] cannot deserialize json '{}' to map: {}",params, e.getMessage());
      return Map.of();
    }
  }

  protected abstract int defaultStatus();

  protected final @NonNull RequestErrorResponse buildRequestErrorResponse(
      @NonNull final RequestErrorException e) {
    // retrieve the RequestError annotation (could be null in case of programming error).
    final RequestError requestError = retrieveRequestError(e.getClass());
    // retrieve the error type annotation properties.
    final Map<String, Object> annotationProperties = annotationProperties(e);

    // retrieve information to be put in the response (body).
    final int status = retrieveStatus(requestError);
    final String label = label(annotationProperties, requestError);
    final Map<String, Object> params = params(annotationProperties);

    final

    final RequestErrorResponse requestErrorResponse = new RequestErrorResponse(
        ZonedDateTime.now(ZoneOffset.UTC), e.getType(), status, e.getRef(),
        traceId(), spanId(), translate(label, params), label, params,
        // TODO verify it works
        e.getClass().getName(),
        e.getErrors());
    return requestErrorResponse;
  }
  //endregion API

  //region Private methods
  private @Nullable String traceId() {
    return id(Tracing::traceId);
  }

  private @Nullable String spanId() {
    return id(Tracing::spanId);
  }

  private @Nullable String id(@NonNull final Function<Tracing, String> id) {
    final Tracing tracing = TracingContainer.get();
    return (tracing == null) ? null : id.apply(tracing);
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

  private @NonNull String translate(@NonNull final String label,
      @NonNull Map<String, Object> params) {
    // FIXME locale deve essere null o devo prendere da quello dello user?
    return translator.translate((Locale)null, label, params);
  }
  //endregion Private methods

}
