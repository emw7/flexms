package com.github.emw7.platform.protocol.api.error;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base class for exception representing dependency(1) error.
 * <p/>
 * Each protocol implementation must extend this class proving meaningful exceptions.
 * <pre>
 * NOTES:
 * [1] Dependency is a third party service
 * </pre>
 */
// TODO implements as extending I18nEnabledExceotion?
public abstract sealed class DependencyErrorException extends Exception
permits ClientDependencyErrorException, ServerDependencyErrorException, UnknownDependencyErrorException {

  /**
   * Raw error response of the dependency.
   */
  private final Object errorResponse;

  /**
   * Service who called the dependency; aka the caller.
   */
  private final String caller;

  /**
   * Dependency (called service) name; aka the callee name.
   */
  private final String serviceName;

  /**
   * Dependency (called service) version; aka the callee version.
   */
  private final String serviceVersion;

  public DependencyErrorException(@NonNull final Object errorResponse,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String message,
      @Nullable final Throwable cause) {
    super(message, cause);
    this.errorResponse = errorResponse;
    this.caller = caller;
    this.serviceName = serviceName;
    this.serviceVersion = serviceVersion;
  }

  //region Getters & Setters
  public @NonNull Object getErrorResponse() {
    return errorResponse;
  }

  public final @NonNull String getCaller() {
    return caller;
  }

  public final @NonNull String getServiceName() {
    return serviceName;
  }

  public final @NonNull String getServiceVersion() {
    return serviceVersion;
  }
  //endregion Getters & Setters

}
