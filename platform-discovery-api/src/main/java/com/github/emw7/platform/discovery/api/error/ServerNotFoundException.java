package com.github.emw7.platform.discovery.api.error;

import com.github.emw7.platform.i18n.I18nLabel;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Requested server has not found in the server registry.
 */
public final class ServerNotFoundException extends ServerRegistryServerException {

  @I18nLabel(params = {"serverName", "serverVersion", "errorReason"})
  private static final String I18N_LABEL = "com.github.emw7.platform.discovery.api.server-not-found";
  //endregion Private static properties

  //region Private static methods
  //endregion Private static methods

  //region Private properties
  private final String serverName;
  private final String serverVersion;
  //endregion Private properties

  //region Constructors
  public ServerNotFoundException(@NonNull final String serverName,
      @NonNull final String serverVersion) {
    this(null, serverName, serverVersion);
  }

  public ServerNotFoundException(@Nullable final Throwable cause,
      @NonNull final String serverName, @NonNull final String serverVersion) {
    super(I18N_LABEL, Map.of("serverName", serverName, "serverVersion", serverVersion), cause);
    this.serverName = serverName;
    this.serverVersion = serverVersion;
  }
  //endregion Constructors

  //region Getters & Setters
  public @NonNull String getServerName() {
    return serverName;
  }

  public @NonNull String getServerVersion() {
    return serverVersion;
  }
  //endregion Getters & Setters

}
