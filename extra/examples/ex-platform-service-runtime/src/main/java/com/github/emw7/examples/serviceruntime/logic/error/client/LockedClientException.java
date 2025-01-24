package com.github.emw7.examples.serviceruntime.logic.error.client;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

// This is deprecated only to mark that it is not used in the example.
@Deprecated
@LockedClientError
public final class LockedClientException extends ClientRequestErrorException {

  public static final Code CODE = new Code("8OSOH");

  public static class ErrorWrapper {

    private final RequestErrorException.Error error;

    private ErrorWrapper(String label, Map<String, Object> params) {
      this.error = new RequestErrorException.Error(label, params);
    }

    private RequestErrorException.Error map() {
      return error;
    }
  }

  public static ErrorWrapper nonRenewableResourceLocking(@NonNull final String resource,
      @NonNull final String owner, int lastedMilliseconds, int remainingMilliseconds) {
    return new ErrorWrapper("app.i18n.error.non-renewable-locking",
        Map.of("resource", resource, "owner", owner, "lastedMilliseconds", lastedMilliseconds,
            "remainingMilliseconds", remainingMilliseconds));
  }

  public static ErrorWrapper renewableResourceLocking(@NonNull final String resource,
      @NonNull final String owner, int consumedMilliseconds, int mustRenewWithinMilliseconds,
      int renewedTimes) {
    return new ErrorWrapper("app.i18n.error.renewable-locking",
        Map.of("resource", resource, "owner", owner, "consumedMilliseconds", consumedMilliseconds,
            "mustRenewWithinMilliseconds", mustRenewWithinMilliseconds, "renewedTimes",
            renewedTimes));
  }

  // constructor
  public LockedClientException(@NonNull final Id id, @NonNull final ErrorWrapper error,
      @NonNull final ErrorWrapper... errors) {
    super(CODE, id, Arrays.stream(errors).map(ErrorWrapper::map).collect(() -> {
      final List<Error> l = new ArrayList<>(1 + errors.length);
      l.add(error.map());
      return l;
    }, List::add, List::addAll));
  }

}
