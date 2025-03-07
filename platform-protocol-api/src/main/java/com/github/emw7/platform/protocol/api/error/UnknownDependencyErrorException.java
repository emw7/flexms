package com.github.emw7.platform.protocol.api.error;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@UnknownDependencyError
public abstract non-sealed class UnknownDependencyErrorException extends DependencyErrorException {

  protected UnknownDependencyErrorException(@NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @Nullable final Throwable cause) {
    super(caller, serviceName, serviceVersion, cause);
  }
}
