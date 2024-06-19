package com.github.emw7.platform.error;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class BadPropertiesClientException extends BadRequestClientException {

  //region Public static methods

  /**
   * Returns an {@link com.github.emw7.platform.error.RequestErrorException.Error} for property
   * violating minimum constraint.
   *
   * @param property the property name
   * @param val      the property value
   * @param min      the admitted minimum
   * @param params   other parameters
   * @return an {@link com.github.emw7.platform.error.RequestErrorException.Error} for property
   * violating minimum constraint
   */
  public static Error min(String property, Number val, Number min, Object... params) {
    return new Error(appClientErrorLabel("property-violate-min"),
        enrichParams(null, "property", property, "val", val, "min", min, params));
  }
  //endregion Public static methods

  //region Private static properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("FBTYV");
  //endregion Private static properties

  //region Constructors
  public BadPropertiesClientException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final List<Error> errors) {
    super(cause, CODE, id, errors);
  }
  //endregion Constructors

}
