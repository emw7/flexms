package com.github.emw7.platform.discovery.api.error;

import com.github.emw7.platform.i18n.I18nLabel;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Server cannot be registered.
 */
public final class CannotRegisterServerException extends ServerRegistryServerException {

  @I18nLabel(params = {"serverName", "serverVersion", "errorReason"})
  private static final String I18N_LABEL = "com.github.emw7.platform.discovery.api.cannot-register-server";
  //endregion Private static properties

  //region Private static methods
  //endregion Private static methods

  //region Private properties
  private final String serverName;
  private final String serverVersion;
  //endregion Private properties

  //region Constructors
  public CannotRegisterServerException(@NonNull final String serverName,
      @NonNull final String serverVersion) {
    this(null, serverName, serverVersion);
  }

  public CannotRegisterServerException(@Nullable final Throwable cause,
      @NonNull final String serverName, @NonNull final String serverVersion) {
    super(I18N_LABEL, Map.of("serverName", serverName, "serverVersion", serverVersion), cause);
    this.serverName = serverName;
    this.serverVersion = serverVersion;
  }
  //endregion Constructors

  //region Getters & Setters
  private String getServerName() {
    return serverName;
  }

  private String getServerVersion() {
    return serverVersion;
  }
  //endregion

}
