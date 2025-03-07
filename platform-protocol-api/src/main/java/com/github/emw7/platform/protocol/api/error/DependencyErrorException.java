package com.github.emw7.platform.protocol.api.error;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base class for exception representing dependency(1) error.
 * <p/>
 * Each protocol implementation must extend this class providing meaningful exceptions.<br/> The
 * original error (that is the exception) that caused this exception must be specified in the
 * {@code cause}. In such a way the upper layers can extract error details if needed.<br/> For
 * example, in the REST stack, an exception of this type is caused by an instance of
 * {@code RestClientException} and such instance must be specified as {@code cause} of this
 * exception.
 * <pre>
 * NOTES:
 * [1] Dependency is a third party service
 * </pre>
 */
// TODO implements as extending I18nEnabledException?
public abstract sealed class DependencyErrorException extends Exception permits
    ClientDependencyErrorException, ServerDependencyErrorException,
    UnknownDependencyErrorException {

  /**
   * The service who called the dependency; aka the caller.
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

  /**
   * Calls {@code super(cause)}.
   *
   * @param caller         the service who called the dependency
   * @param serviceName    the name of the called service
   * @param serviceVersion the version of the called service
   * @param cause          the cause of this error, in other words, the raw error response of the
   *                       dependency
   */
  protected DependencyErrorException(@NonNull final String caller,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @Nullable final Throwable cause) {
    super(( cause == null ) ? null : cause.getLocalizedMessage(), cause);
    this.caller = caller;
    this.serviceName = serviceName;
    this.serviceVersion = serviceVersion;
  }

  //region Getters & Setters
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
