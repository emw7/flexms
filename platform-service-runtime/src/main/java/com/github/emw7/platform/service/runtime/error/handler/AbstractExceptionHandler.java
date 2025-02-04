package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.common.request.error.ServiceCoreCommonRequestErrorConstants;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.log.tracing.LogTracingUtil;
import com.github.emw7.platform.log.tracing.TracerContainer;
import com.github.emw7.platform.service.core.common.request.error.RequestError;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import com.github.emw7.platform.service.core.request.context.RequestContextHolder;
import io.micrometer.tracing.Tracer;
import java.lang.annotation.Annotation;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

// WARNING TEST: it is not easy to test because uses static/singleton TracerContainer.
/**
 * Base for both client and server exception handler.
 */
public abstract sealed class AbstractExceptionHandler permits AbstractClientExceptionHandler,
    AbstractServerExceptionHandler {

  //region Private static properties
  private static final Logger log = LoggerFactory.getLogger(AbstractExceptionHandler.class);
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
  private final @NonNull Map<String, Object> annotationProperties(
      @NonNull final RequestErrorException e) {

    final Annotation requestErrorAnnotation = findAnnotation(e.getClass(), RequestError.class);
    return (requestErrorAnnotation != null) ? AnnotationUtils.getAnnotationAttributes(
        requestErrorAnnotation) : Map.of();
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
      return ServiceCoreCommonRequestErrorConstants.DEFAULT_ERROR_LABEL;
    }
  }

  private final @NonNull Map<String, Object> params(
      @NonNull final Map<String, Object> annotationProperties) {
    final String params = Optional.ofNullable((String) annotationProperties.get("params"))
        .filter(v -> !StringUtils.isEmpty(v)).orElse("{}");
    try {
      return objectReader.readValue(params);
    } catch (JsonProcessingException e) {
      EventLogger.notice(log,"[JSON-PROCESSING] cannot deserialize json '{}' to map '{}'; returning empty map as fallback",
          params, e.getMessage()).warn().log();
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

    boolean isService = isService();
    final RequestErrorResponse requestErrorResponse = new RequestErrorResponse(
        ZonedDateTime.now(ZoneOffset.UTC), e.getType(), status, e.getRef(), traceId(), spanId(),
        translate(label, params), label, e.getErrors());
    return requestErrorResponse;
  }
  //endregion API

  //region Private methods
  private boolean isService() {
    if (RequestContextHolder.get() == null) {
      return false;
    } else {
      return RequestContextHolder.get().caller().isService();
    }
  }

  private @Nullable String traceId() {
    return id(LogTracingUtil::traceId);
  }

  private @Nullable String spanId() {
    return id(LogTracingUtil::spanId);
  }

  private @Nullable String id(@NonNull final Function<Tracer, String> id) {
    final Tracer tracer = TracerContainer.getTracer();
    return id.apply(tracer);
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
    return translator.translate(RequestContextHolder.get().locale(), label, params);
  }
  //endregion Private methods

}
