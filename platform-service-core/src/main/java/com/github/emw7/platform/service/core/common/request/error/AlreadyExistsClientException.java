package com.github.emw7.platform.service.core.common.request.error;

//import static com.github.emw7.platform.error.RequestErrorException.enrichParams;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.service.core.common.request.error.category.AlreadyExists;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The client error exception tied to {@link AlreadyExists} category.
 */
@AlreadyExists
public abstract class AlreadyExistsClientException extends ResourceIdClientException {

  //region Error mapper
  public static <T extends AlreadyExistsClientException, P> AlreadyExistsResponseToExceptionMapper<T, P> mapper(
      @NonNull final Class<T> clazz, @NonNull final Class<P> parameterType) {
    return new AlreadyExistsResponseToExceptionMapper<>(clazz, parameterType);
  }

  public static class AlreadyExistsResponseToExceptionMapper<T extends AlreadyExistsClientException, P> extends
      AbstractsSingleParameterConstructorResponseToExceptionMapper<T, P> {

    private AlreadyExistsResponseToExceptionMapper(@NonNull final Class<T> clazz,
        @NonNull final Class<P> parameterType) {
      super(CODE, clazz, parameterType, RESOURCE_ID_KEY);
    }
  }
  //endregion Error mapper

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("D4FAA");

  @I18nLabel(params = {})
  private static final String LABEL_BASE = clientRequestErrorBaseLabel("already-exists");

  protected AlreadyExistsClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId, @Nullable final Map<String, Object> params) {
    super(CODE, id, LABEL_BASE + '.' + resourceName, resourceName, resourceId, params);
  }

}
