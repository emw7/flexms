package com.github.emw7.platform.discovery.api.error;

import com.github.emw7.platform.i18n.I18nEnabledException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class ServerRegistryServerException extends I18nEnabledException {

  private final String i18nLabel;

  public ServerRegistryServerException(@NonNull final String i18nLabel,
      @Nullable final Map<String, Object> params) {
    super(i18nLabel, params);
    this.i18nLabel= i18nLabel;
  }

  public ServerRegistryServerException(@NonNull final String i18nLabel,
      @Nullable final Map<String, Object> params,
      final Throwable cause) {
    super(i18nLabel, params, cause);
    this.i18nLabel= i18nLabel;
  }

}
