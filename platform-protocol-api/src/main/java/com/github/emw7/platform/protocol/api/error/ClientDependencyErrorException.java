package com.github.emw7.platform.protocol.api.error;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@ClientDependencyError
public abstract non-sealed class ClientDependencyErrorException extends DependencyErrorException {

  public ClientDependencyErrorException(@NonNull final Object errorResponse,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String message,
      @Nullable final Throwable cause) {
    super(errorResponse, caller, serviceName, serviceVersion, message, cause);
  }



}
