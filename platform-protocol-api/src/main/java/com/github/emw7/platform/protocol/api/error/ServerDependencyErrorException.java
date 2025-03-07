package com.github.emw7.platform.protocol.api.error;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@ServerDependencyError
public abstract non-sealed class ServerDependencyErrorException extends DependencyErrorException {

  protected ServerDependencyErrorException(@NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @Nullable final Throwable cause) {
    super(caller, serviceName, serviceVersion, cause);
  }

}
