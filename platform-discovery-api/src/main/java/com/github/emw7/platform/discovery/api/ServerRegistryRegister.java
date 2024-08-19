package com.github.emw7.platform.discovery.api;

import com.github.emw7.platform.discovery.api.error.CannotRegisterServerException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface ServerRegistryRegister {

  /**
   * Registers the server into the register.
   *
   * @param serviceName    name of the server
   * @param serviceVersion version of the server
   * @param url            url at which the server will respond at
   * @param context        context that implementation can manage
   * @throws CannotRegisterServerException if the requested server can *NOT* be registered
   */
  void register(@NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String url, @Nullable final Context context) throws CannotRegisterServerException;

}
