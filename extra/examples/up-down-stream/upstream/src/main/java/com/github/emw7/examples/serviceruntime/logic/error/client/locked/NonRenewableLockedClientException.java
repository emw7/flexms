package com.github.emw7.examples.serviceruntime.logic.error.client.locked;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import java.util.Map;
import org.springframework.lang.NonNull;

/**
 * Specialization of locked client error for non-renewable specialization.
 * <p>
 * An alternative to
 * {@code throw new LockedClientException(new Id("ISVXB"),
 * LockedClientException.nonRenewableResourceLocking("a-non-renewable-locking-resource", "other",
 * 15, 85));}
 */
public final class NonRenewableLockedClientException extends LockedClientException {

  public NonRenewableLockedClientException(@NonNull final Id id, @NonNull final String resource,
      @NonNull final String owner, int lastedMilliseconds, int remainingMilliseconds) {
    super(id, new RequestErrorException.Error("app.i18n.error.non-renewable-locking",
        Map.of("resource", resource, "owner", owner, "lastedMilliseconds", lastedMilliseconds,
            "remainingMilliseconds", remainingMilliseconds)));
  }

}
