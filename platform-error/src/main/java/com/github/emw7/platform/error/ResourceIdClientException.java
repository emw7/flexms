package com.github.emw7.platform.error;

//import static com.github.emw7.platform.error.RequestErrorException.enrichParams;

import com.github.emw7.platform.error.category.NotFound;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@NotFound
public abstract class ResourceIdClientException extends ClientRequestErrorException {

  public static final String RESOURCE_NAME_KEY = "resourceName";
  public static final String RESOURCE_ID_KEY = "resourceId";

  protected ResourceIdClientException(@NonNull final Code code, @NonNull final Id id,
      @NonNull final String label, @NonNull final String resourceName,
      @NonNull final Object resourceId, @Nullable final Map<String, Object> params) {
    super(code, id, new Error(label,
        enrichParams(params, RESOURCE_NAME_KEY, resourceName, RESOURCE_ID_KEY, resourceId)));
  }

}
