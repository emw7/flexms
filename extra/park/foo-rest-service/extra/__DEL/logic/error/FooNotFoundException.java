package com.github.emw7.foo.service.foo.logic.error;

import com.github.emw7.foo.model.Foo;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class FooNotFoundException extends NotFoundClientException {

  public FooNotFoundException(
      @NonNull final Id id,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(id, Foo.RESOURCE_NAME, resourceId, params);
  }
}
