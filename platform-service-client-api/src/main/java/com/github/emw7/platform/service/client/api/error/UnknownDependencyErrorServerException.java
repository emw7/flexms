package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The dependency answered with an error that either cannot be managed or do not know how to manage.
 */
public final class UnknownDependencyErrorServerException extends
    DependencyErrorServerException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("IY8ZM");
  //endregion Private static final properties

  @I18nLabel(label = "com.github.emw7.platform.service.client.api.unknown-dependency-error",
  params={CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY, CAUSE_KEY})
  private static final String I18N_LABEL = I18nLabelPrefixes.PLATFORM_PREFIX +
      "service.client.api." + "unknown-dependency-error";
  //endregion Private static final properties

  //region Constructors
  public UnknownDependencyErrorServerException(@NonNull final Throwable cause, @NonNull final Id id,
      @NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion) {
    super(cause, CODE, id, I18N_LABEL, caller, serviceName, serviceVersion, null);
    // TODO remove comment
        //new Error(I18N_LABEL, Map.of("cause",cause.getLocalizedMessage())));
  }
  //endregion Constructors

}
