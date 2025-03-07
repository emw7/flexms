package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.core.array.ArrayUtil;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.i18n.TranslatorContainer;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class AbstractConstructorResponseToExceptionMapper<T extends RequestErrorException> extends AbstractResponseToExceptionMapper<T> {

  private static final Logger log = LoggerFactory.getLogger(
      AbstractConstructorResponseToExceptionMapper.class);

  /**
   *
   * @return
   * @throws RuntimeException in case constructor cannot be retrieved
   */
  private static <T extends RequestErrorException> Constructor<T> retrieveConstructor (@NonNull final Class<T> clazz,
      @NonNull final Class<?>... parameterTypes) throws RuntimeException{
    try {
     return clazz.getConstructor(parameterTypes);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(TranslatorContainer.getTranslator()
          .translate(LocaleContextHolder.getLocale(),
              // TODO non va bene il messaggio qui... NON stiamo istanziando... ma cercando...
              "com.github.emw7.platform.i18n.error.cannot-instantiate.error-response-to-exception-mapper",
              Map.of("class", clazz.getName(), "error",
                  TranslatorContainer.getTranslator().translate(Locale.getDefault(),
                      "com.github.emw7.platform.i18n.error.cannot-find-constructor",
                      Map.of("signature", Arrays.stream(parameterTypes).map(Class::getName).collect(
                          Collectors.joining(",", "(", ")")))))), e);
    }
  }

  private final Constructor<T> constructor;

  protected AbstractConstructorResponseToExceptionMapper(@NonNull final Code code, @NonNull final Class<T> clazz,
      @NonNull final Class<?>... parameterTypes) {
    this(code,retrieveConstructor(clazz, parameterTypes));
  }

  protected AbstractConstructorResponseToExceptionMapper(@NonNull final Code code, @NonNull final Constructor<T> constructor) {
    super(code);
    this.constructor= constructor;
  }

  @Override
  protected final @NonNull T _map(@NonNull final RequestErrorResponse requestErrorResponse) throws CannotMapException {
    try {
      final Object[] args= retrieveArgsForConstructor(requestErrorResponse);
      return constructor.newInstance(args);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      // TODO add warn for error in instantiating... e mettere in log anche la lista di (almeno il size) di args
      throw new CannotMapException("",e);
    }
  }

  /**
   *
   * @return the arguments to be passed to the constructor
   */
  protected abstract Object[] retrieveArgsForConstructor(@NonNull final RequestErrorResponse requestErrorResponse);

}
