package com.github.emw7.platform.error;

//import static com.github.emw7.platform.error.RequestErrorException.enrichParams;
import com.github.emw7.platform.error.category.AlreadyExists;
import com.github.emw7.platform.error.category.NotFound;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// TODO gestione I18N LABEL come per NotFound?
@AlreadyExists
public abstract class AlreadyExistsClientException extends ResourceIdClientException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("D4FAA");

  private static final String LABEL_BASE = appClientErrorLabel("already-exists");

  // TODO [DOC]: error label is "app.error.client." + "not-found" + "." + resourceName.
  protected AlreadyExistsClientException(@NonNull final Id id, @NonNull final String resourceName,
      @NonNull final Object resourceId,
      @Nullable final Map<String, Object> params) {
    super(CODE, id, LABEL_BASE + '.' + resourceName, resourceName, resourceId, params);
  }

}
