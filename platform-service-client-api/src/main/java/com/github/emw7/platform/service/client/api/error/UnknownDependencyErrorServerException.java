package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The dependency answered with an error that either cannot be managed or do not know how to manage.
 */
public final class UnknownDependencyErrorServerException extends
    DependencyErrorServerException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("IY8ZM");
  //endregion Private static final properties

  //region Private properties
  private final String causeRef;
  //endregion Private properties

  @I18nLabel(params = { CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY, CAUSE_KEY })
  private static final String I18N_LABEL = "com.github.emw7.platform.service.client.api.unknown-dependency-error";
  //endregion Private static final properties

  //region Constructors
  public UnknownDependencyErrorServerException(@NonNull final DependencyErrorException cause, @NonNull final Id id,
      @Nullable final String causeRef) {
    super(cause, CODE, id, I18N_LABEL, cause.getCaller(), cause.getServiceName(),
        cause.getServiceVersion(), enrichParams(null,"causeRef",causeRef));
    this.causeRef= causeRef;
  }

//  public UnknownDependencyErrorServerException(@NonNull final DependencyErrorException cause, @NonNull final Id id) {
//    this(cause, id, null);
//  }
  //endregion Constructors

  //region Getters & Setters
  private @Nullable String getCauseRef() {
    return causeRef;
  }
  //endregion Getters & Setters

}
