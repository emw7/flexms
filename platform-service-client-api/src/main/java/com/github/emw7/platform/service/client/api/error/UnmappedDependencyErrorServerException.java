package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import org.springframework.lang.NonNull;

/**
 * The dependency answered with an error that has not been mapped neither by platform nor by app to
 * a {@link com.github.emw7.platform.error.RequestErrorException}
 */
public final class UnmappedDependencyErrorServerException extends DependencyErrorServerException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("TLR26");

  @I18nLabel(label = "com.github.emw7.platform.service.client.api.unmapped-dependency-error", params = {
      CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY})
  private static final String I18N_LABEL =
      I18nLabelPrefixes.PLATFORM_PREFIX +
          "service.client.api." + "unmapped-dependency-error";
  //endregion Private static final properties

  //region Constructors
  public UnmappedDependencyErrorServerException(@NonNull final Throwable cause, @NonNull final Id id,
      @NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion) {
    super(cause, CODE, id, I18N_LABEL, caller, serviceName, serviceVersion, null);
  }
  //endregion Constructors

}
