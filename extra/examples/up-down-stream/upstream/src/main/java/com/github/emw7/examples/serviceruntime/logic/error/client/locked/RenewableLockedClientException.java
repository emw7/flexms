package com.github.emw7.examples.serviceruntime.logic.error.client.locked;

import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import java.util.Map;
import org.springframework.lang.NonNull;


/**
 * Specialization of locked client error for renewable specialization.
 * <p>
 * An alternative to
 * {@code LockedClientException(new Id("3M9DD"),
 * LockedClientException.renewableResourceLocking("a-renewable-locking-resource", "other", 10,
 * 1));}
 */
// This is deprecated only to mark that it is not used in the example.
@Deprecated
public final class RenewableLockedClientException extends LockedClientException {

  public RenewableLockedClientException(@NonNull final Id id, @NonNull final String resource,
      @NonNull final String owner, int consumedMilliseconds, int mustRenewWithinMilliseconds,
      int renewedTimes) {
    super(id, new RequestErrorException.Error("app.i18n.error.renewable-locking",
        Map.of("resource", resource, "owner", owner, "consumedMilliseconds", consumedMilliseconds,
            "mustRenewWithinMilliseconds", mustRenewWithinMilliseconds, "renewedTimes",
            renewedTimes)));
  }

}
