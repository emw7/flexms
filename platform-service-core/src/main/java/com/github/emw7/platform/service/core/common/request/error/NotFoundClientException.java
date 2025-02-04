package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.category.NotFound;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The client error exception tied to {@link NotFound} category.
 */
public abstract class NotFoundClientException extends ResourceIdClientException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("RY19P");

  private static final String LABEL_BASE = clientRequestErrorBaseLabel("not-found");

  // TODO [DOC]: error label is "app.error.client." + "not-found" + "." + resourceName.
  protected NotFoundClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(CODE, id, LABEL_BASE + '.' + resourceName, resourceName, resourceId, params);
  }

}
