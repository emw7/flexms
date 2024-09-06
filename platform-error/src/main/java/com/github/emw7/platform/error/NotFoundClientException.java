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
  public static final Code CODE = new Code("RY19P");

  private static final String LABEL_BASE = appClientErrorLabel("not-found");

  // TODO [DOC]: error label is "app.error.client." + "not-found" + "." + resourceName.
  protected NotFoundClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(CODE, id, LABEL_BASE + '.' + resourceName, resourceName, resourceId, params);
  }

}
