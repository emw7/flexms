package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import java.sql.Ref;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The exception that describes that an error occurred in the calling stack of the downstream.
 * <p/>
 * If service A calls service B (the downstream) and service B calls service C the error that
 * occurred in calling C it is described by A as an instance of this class.
 */
public final class DownstreamStackDependencyErrorServerException extends
    DependencyErrorServerException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("5XDNA");
  //endregion Private static final properties

  @I18nLabel(label = "com.github.emw7.platform.service.client.api.downstream-stack-dependency-error",
  params={CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY, CAUSE_KEY})
  private static final String I18N_LABEL = I18nLabelPrefixes.PLATFORM_PREFIX +
      "service.client.api." + "downstream-stack-dependency-error";
  //endregion Private static final properties

  //region Private properties
  private final String causeRef;
  //endregion Private properties

  //region Constructors
  public DownstreamStackDependencyErrorServerException(@NonNull final DependencyErrorException cause, @NonNull final Id id,
      @Nullable final String causeRef) {
    super(cause, CODE, id, I18N_LABEL, cause.getCaller(), cause.getServiceName(), cause.getServiceVersion(),
        enrichParams(null, "causeRef", causeRef));
    this.causeRef= causeRef;
  }
  //endregion Constructors

}
