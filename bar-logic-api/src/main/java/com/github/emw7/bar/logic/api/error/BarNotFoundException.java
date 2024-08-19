package com.github.emw7.bar.logic.api.error;

import com.github.emw7.bar.model.Bar;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class BarNotFoundException extends NotFoundClientException {

  public BarNotFoundException(
      @NonNull final Id id,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(id, Bar.RESOURCE_NAME, resourceId, params);
  }
}
