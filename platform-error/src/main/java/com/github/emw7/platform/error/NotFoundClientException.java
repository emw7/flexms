package com.github.emw7.platform.error;

//import static com.github.emw7.platform.error.RequestErrorException.enrichParams;
import com.github.emw7.platform.error.category.NotFound;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@NotFound
public abstract class NotFoundClientException extends ResourceIdClientException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("RY19P");

  private static final String LABEL = appClientErrorLabel("resource-not-found");

  protected NotFoundClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    this(id,LABEL, resourceName, resourceId, params);
  }

  protected NotFoundClientException(@NonNull final Id id, @NonNull final String label, @NonNull final String resourceName,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(CODE, id, label, resourceName, resourceId, params);
  }

}
