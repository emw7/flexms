package com.github.emw7.examples.serviceruntime.logic.error.server;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import java.util.Map;
import org.springframework.lang.NonNull;

public final class ResourcesExhaustedServerRequestErrorException extends ServerRequestErrorException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("J5ESE");
  //endregion Private static final properties

  @I18nLabel(params={"resource"})
  private static final String I18N_LABEL = "app.i18n.error.resources-exhausted";
  //endregion Private static final properties

  //region Constructors
  public ResourcesExhaustedServerRequestErrorException(@NonNull final Id id, @NonNull final String resource) {
    super(CODE, id, new Error(I18N_LABEL, Map.of("resource",resource)));
  }
  //endregion Constructors
}
