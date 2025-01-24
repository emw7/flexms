package com.github.emw7.examples.serviceruntime.logic.error.client.locked;

import com.github.emw7.examples.serviceruntime.logic.error.client.LockedClientError;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import org.springframework.lang.NonNull;

/**
 * The base class for locked client error The base class for locked client error.
 * <p>
 * Extends this class to specialize the error.
 */
@LockedClientError
public abstract class LockedClientException extends ClientRequestErrorException {

  public static final Code CODE = new Code("8OSOH");

  // constructor
  protected LockedClientException(@NonNull final Id id, @NonNull final Error error) {
    super(CODE, id, error);
  }

}
