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
public abstract class DependencyErrorException extends Exception  {

  /**
   * Raw error response of the dependency.
   */
  private final Object errorResponse;

  public DependencyErrorException(@Nullable final Object errorResponse, @NonNull final String message, @Nullable final Throwable cause) {
    super(message, cause);
    this.errorResponse= errorResponse;
  }

  //region Getters & Setters
  public @Nullable Object getErrorResponse() {
    return errorResponse;
  }

  //endregion Getters & Setters

}
