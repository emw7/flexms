package com.github.emw7.platform.discovery.api;

import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import org.springframework.lang.NonNull;

public interface ServerRegistryDiscover {

  /**
   * Returns the server information including the location of the specified server.
   *
   * @param serviceName    name of the searched server
   * @param serviceVersion version od the searched server
   * @return he url at which provided server of the provided version responds to
   * @throws ServerNotFoundException if {@code serviceName} and {@serviceName} can *NOT* be found
   */
  @NonNull
  Server discover(@NonNull final String serviceName, @NonNull final String serviceVersion)
      throws ServerNotFoundException;

}
