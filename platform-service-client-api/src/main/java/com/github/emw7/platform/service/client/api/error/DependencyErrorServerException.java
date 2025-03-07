package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class DependencyErrorServerException extends ServerRequestErrorException {

  //region Public static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("HM9BC");
  //endregion Public static final properties

  //region Private static final properties
  private static final String CALLER_KEY = "caller";
  private static final String SERVICE_NAME_KEY = "serviceName";
  private static final String SERVICE_VERSION_KEY = "serviceVersion";
  private static final String CAUSE_MSG_KEY = "cause";

  @I18nLabel(params = {CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY, CAUSE_MSG_KEY})
  private static final String I18N_LABEL = "com.github.emw7.platform.i18n.error.request.server.dependency-error";
  //endregion Private static final properties

  //region private final properties
  private final RequestErrorResponse requestErrorResponse;
  //endregion private final properties

  //region Constructors
  public DependencyErrorServerException(@NonNull final DependencyErrorException cause,
      @NonNull final Id id, @Nullable final RequestErrorResponse requestErrorResponse) {
    super(cause, CODE, id, new Error(I18N_LABEL,
        Map.of(CALLER_KEY, cause.getCaller(), SERVICE_NAME_KEY, cause.getServiceName(),
            SERVICE_VERSION_KEY, cause.getServiceVersion(), CAUSE_MSG_KEY,
            cause.getLocalizedMessage())));
    this.requestErrorResponse = requestErrorResponse;
  }
  //endregion Constructors

  //region Getters & Setters
  public @Nullable RequestErrorResponse getRequestErrorResponse() {
    return requestErrorResponse;
  }

  public final @NonNull String getCaller() {
    return ((DependencyErrorException) getCause()).getCaller();
  }

  public final @NonNull String getServiceName() {
    return ((DependencyErrorException) getCause()).getServiceName();
  }

  public final @NonNull String getServiceVersion() {
    return ((DependencyErrorException) getCause()).getServiceVersion();
  }
  //endregion Getters & Setters

}
