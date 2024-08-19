package com.github.emw7.platform.error;

import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Server exception to be thrown when server could NOT be found in the server registry.
 */
public final class ServiceNotFoundServerException extends ServerRequestErrorException {

  public static final String CALLER_KEY = "caller";
  public static final String SERVICE_NAME_KEY = "serviceName";
  public static final String SERVICE_VERSION_KEY = "serviceVersion";

  //region Private static properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("MVI5T");

  @I18nLabel(label = "com.github.emw7.platform.error.service-not-found", params = {
      CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY})
  private static final String I18N_LABEL =
      I18nLabelPrefixes.PLATFORM_PREFIX + "error." + "service-not-found";
  //endregion Private static properties

  public ServiceNotFoundServerException(@Nullable Throwable cause, @NonNull final Id id,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @Nullable final Map<String, Object> params) {
    super(cause, CODE, id, new Error(I18N_LABEL,
        enrichParams(params, CALLER_KEY, caller, SERVICE_NAME_KEY, serviceName, SERVICE_VERSION_KEY,
            serviceVersion)));
  }

}
