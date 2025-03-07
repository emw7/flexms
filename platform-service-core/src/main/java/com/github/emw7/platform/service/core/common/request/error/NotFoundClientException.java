package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.NotFoundClientException.NotFoundResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.category.NotFound;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The client error exception tied to {@link NotFound} category.
 */
@NotFound
public abstract class NotFoundClientException extends ResourceIdClientException {

  //region Error mapper
  public static <T extends NotFoundClientException, P> NotFoundClientException.NotFoundResponseToExceptionMapper<T, P> mapper(
      @NonNull final Class<T> clazz, @NonNull final Class<P> parameterType) {
    return new NotFoundClientException.NotFoundResponseToExceptionMapper<>(clazz, parameterType);
  }

  public static class NotFoundResponseToExceptionMapper<T extends NotFoundClientException, P> extends
      AbstractsSingleParameterConstructorResponseToExceptionMapper<T, P> {

    private NotFoundResponseToExceptionMapper(@NonNull final Class<T> clazz,
        @NonNull final Class<P> parameterType) {
      super(CODE, clazz, parameterType, RESOURCE_ID_KEY);
    }
  }
  //endregion Error mapper

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("RY19P");

  private static final String LABEL_BASE = clientRequestErrorBaseLabel("not-found");

  // TODO [DOC]: error label is "app.error.client." + "not-found" + "." + resourceName.
  protected NotFoundClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId) {
    super(CODE, id, LABEL_BASE + '.' + resourceName, resourceName, resourceId, null);
  }

}
