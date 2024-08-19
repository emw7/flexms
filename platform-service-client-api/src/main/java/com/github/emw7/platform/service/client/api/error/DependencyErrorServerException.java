package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class DependencyErrorServerException extends ServerRequestErrorException {

  //region Public static properties
  public static final String CALLER_KEY = "caller";
  public static final String SERVICE_NAME_KEY = "serviceName";
  public static final String SERVICE_VERSION_KEY = "serviceVersion";
  public static final String CAUSE_KEY = "cause";
  //endregion Public static properties

  //region Constructors
  protected DependencyErrorServerException(@NonNull final Throwable cause, @NonNull final Code code,
      @NonNull final Id id, @NonNull final String i18nLabel, @NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @Nullable final Map<String, Object> params) {
    this(cause, code, id, i18nLabel, caller, serviceName, serviceVersion,
        cause.getLocalizedMessage(), params);
  }

  protected DependencyErrorServerException(@NonNull final Throwable cause, @NonNull final Code code,
      @NonNull final Id id, @NonNull final String i18nLabel, @NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String causeMsg, @Nullable final Map<String, Object> params) {
    super(cause, code, id, new Error(i18nLabel,
        enrichParams(params, CALLER_KEY, caller, SERVICE_NAME_KEY, serviceName, SERVICE_VERSION_KEY,
            serviceVersion, CAUSE_KEY, causeMsg)));
  }
  //endregion Constructors

}
