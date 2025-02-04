package com.github.emw7.platform.service.core.common.request.error;


import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The client error exception tied to
 * {@link com.github.emw7.platform.service.core.common.request.error.category.BadRequest} category
 * specific for properties that does not satisfy constraints.
 */
public final class BadPropertiesClientException extends BadRequestClientException {

  //region Public static types
  public static class BadPropertyError {

    private final String label;
    private final Map<String, Object> params;

    private BadPropertyError(@NonNull final String label, @NonNull final Map<String, Object> params) {
      this.label= label;
      this.params= params;
    }

    public Error map() {
      return new Error(this.label, this.params);
    }
  }

  //endregion Public static types

  //region Public static methods
  public static BadPropertyError min(String property, Number val, Number min) {
    return new BadPropertiesClientException.BadPropertyError(clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-min"),
        Map.of("property", property, "val", val, "min", min));
  }

  public static BadPropertyError mustBeNotNull(String property) {
    return new BadPropertyError(clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-must-be-not-null"),
        Map.of("property", property));
  }

  public static BadPropertyError mustBeNull(String property) {
    return new BadPropertyError(clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-must-be-null-null"),
        Map.of("property", property));
  }
  //endregion Public static methods

  //region Private static properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("FBTYV");
  //endregion Private static properties

  //region Constructors
  public BadPropertiesClientException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final BadPropertyError error, BadPropertyError... errors) {
    super(cause, CODE, id,
        Arrays.stream(errors)
            .map(BadPropertyError::map)
            .collect(() -> { final ArrayList<Error> r= new ArrayList<>(errors.length+1); r.add(error.map()); return r; },ArrayList::add,
                     ArrayList::addAll));
  }
  //endregion Constructors

}
