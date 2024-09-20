package com.github.emw7.platform.discovery.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Requested server has not found in the server registry.
 */
public final class ServerNotFoundException extends ServerRegistryServerException {

  //region Private static properties
  // generated with https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new..
  private static final Code CODE = new Code("4EF1I");

  @I18nLabel(params = {"serverName", "serverVersion", "errorReason"})
  private static final String I18N_LABEL = "com.github.emw7.platform.discovery.api.server-not-found";
  //endregion Private static properties

  //region Private static methods
  //endregion Private static methods

  private final String serverName;
  private final String serverVersion;

  public ServerNotFoundException(@NonNull final Id id, @NonNull final String serverName,
      @NonNull final String serverVersion) {
    this(null, id, serverName, serverVersion);
  }

  public ServerNotFoundException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final String serverName, @NonNull final String serverVersion) {
    super(I18N_LABEL, Map.of("serverName", serverName, "serverVersion", serverVersion), cause);
    this.serverName = serverName;
    this.serverVersion = serverVersion;
  }

  //region Getters & Setters
  public @NonNull String getServerName() {
    return serverName;
  }

  public @NonNull String getServerVersion() {
    return serverVersion;
  }
  //endregion Getters & Setters

}
