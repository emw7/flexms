package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.i18n.TranslatorContainer;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;

public abstract class AbstractsSingleParameterConstructorResponseToExceptionMapper<T extends RequestErrorException, P> extends
    AbstractConstructorResponseToExceptionMapper<T> {

  private static final Logger log = LoggerFactory.getLogger(
      AbstractsSingleParameterConstructorResponseToExceptionMapper.class);

  private final String key;

  protected AbstractsSingleParameterConstructorResponseToExceptionMapper(@NonNull final Code code,
      @NonNull final Class<T> clazz, @NonNull final Class<P> parameterType,
      /*@NonNull final Constructor<T> constructor,*/
      @NonNull final String key) {
    super(code, clazz, parameterType);
    this.key = key;
  }


  /**
   * @return the arguments to be passed to the constructor
   */
  protected final Object[] retrieveArgsForConstructor(
      @NonNull final RequestErrorResponse requestErrorResponse) {

    final Object[] args = new Object[1];
    args[0] = 0;

    List<Error> errors = requestErrorResponse.errors();
    //noinspection ConstantValue
    if (errors == null || errors.isEmpty()) {
      EventLogger.notice(log, "Error response has either null or empty error but 1 must be present")
          .warn().log();
    } else {
      Map<String, Object> params = errors.getFirst().params();
      if (params == null || params.isEmpty()) {
        EventLogger.notice(log, "Error response first error has either null or empty params").warn()
            .log();
      } else {
        if (params.containsKey(key)) {
          args[0] = params.get(key);
        } else {
          EventLogger.notice(log, "Error response first error params does not contains {} key", key)
              .warn().log();
        }
      }
    }
    return args;
  }

}
