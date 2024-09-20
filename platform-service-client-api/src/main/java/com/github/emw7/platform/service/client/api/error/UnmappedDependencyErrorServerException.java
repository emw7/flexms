package com.github.emw7.platform.service.client.api.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The dependency answered with an error that has not been mapped neither by platform nor by app to
 * a {@link com.github.emw7.platform.error.RequestErrorException}
 */
public final class UnmappedDependencyErrorServerException extends DependencyErrorServerException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("TLR26");

  @I18nLabel(params = { CALLER_KEY, SERVICE_NAME_KEY, SERVICE_VERSION_KEY })
  private static final String I18N_LABEL = "com.github.emw7.platform.service.client.api.unmapped-dependency-error";
  //endregion Private static final properties

  //region Private properties
  private final Class<? extends Annotation> causeCategory;
  private final String causeRef;
  //endregion Private properties

  //region Constructors

  /**
   *
   * @param cause
   * @param id
   * @param causeCategory the category can be {@code null} in case
   *                      received response error response contains a ref different from {@code null} but cannot be mapped to a known category
   * @param causeRef
   */
  public UnmappedDependencyErrorServerException(@NonNull final DependencyErrorException cause, @NonNull final Id id,
      @Nullable final Class<? extends Annotation> causeCategory, @NonNull final String causeRef) {
    super(cause, CODE, id, I18N_LABEL, cause.getCaller(), cause.getServiceName(), cause.getServiceVersion(),
        enrichParams(null,"causeCategory", causeCategory, "causeRef", causeRef));
    this.causeCategory = causeCategory;
    this.causeRef = causeRef;
  }
  //endregion Constructors

  //region Getters & Setters
  public @NonNull Class<? extends Annotation> getCauseCategory() {
    return causeCategory;
  }

  public @Nullable String getCauseRef() {
    return causeRef;
  }

  //endregion Getters & Setters

}
