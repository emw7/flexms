package com.github.emw7.platform.discovery.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Server cannot be registered
 */
public final class CannotRegisterServerException extends ServerRegistryServerException {

  //region Private static properties
  // generated with https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new..
  private static final Code CODE = new Code("YFL5LF");

  @I18nLabel(params = {"serverName", "serverVersion", "errorReason"})
  private static final String I18N_LABEL = "com.github.emw7.platform.discovery.api.cannot-register-server";
  //endregion Private static properties

  //region Private static methods
  //endregion Private static methods

  private final String serverName;
  private final String serverVersion;

  public CannotRegisterServerException(@NonNull final Id id, @NonNull final String serverName,
      @NonNull final String serverVersion) {
    this(null, id, serverName, serverVersion);
  }

  public CannotRegisterServerException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final String serverName, @NonNull final String serverVersion) {
    super(I18N_LABEL, Map.of("serverName", serverName, "serverVersion", serverVersion), cause);
    this.serverName = serverName;
    this.serverVersion = serverVersion;
  }

}
