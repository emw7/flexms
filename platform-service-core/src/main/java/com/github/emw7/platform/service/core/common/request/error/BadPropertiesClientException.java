package com.github.emw7.platform.service.core.common.request.error;


import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The client error exception tied to
 * {@link com.github.emw7.platform.service.core.common.request.error.category.BadRequest} category
 * specific for properties that does not satisfy constraints.
 */
public final class BadPropertiesClientException extends BadRequestClientException {

  //region Error mapper
  public static BadPropertiesClientException.BadPropertiesResponseToExceptionMapper mapper() {
    return new BadPropertiesClientException.BadPropertiesResponseToExceptionMapper();
  }

  public static class BadPropertiesResponseToExceptionMapper extends
      AbstractResponseToExceptionMapper<BadPropertiesClientException> {

    private static final Logger log = LoggerFactory.getLogger(
        BadPropertiesResponseToExceptionMapper.class);

    private BadPropertiesResponseToExceptionMapper() {
      super(BadPropertiesClientException.CODE);
    }

    @SuppressWarnings({"ConstantValue", "DataFlowIssue"})
    @Override
    protected final @NonNull BadPropertiesClientException _map(
        @NonNull final RequestErrorResponse requestErrorResponse) {
      List<Error> errors = requestErrorResponse.errors();

      if (errors == null || errors.isEmpty()) {
        EventLogger.notice(log,
                "Error response has either null or empty error but at least 1 must be present").warn()
            .log();
        errors = Optional.ofNullable(errors).orElse(List.of());
      }
      return new BadPropertiesClientException(errors);
    }
  }

  //endregion Error mapper

  //region Public static types
  public static class BadPropertyError {

    private final String label;
    private final Map<String, Object> params;

    private BadPropertyError(@NonNull final String label,
        @NonNull final Map<String, Object> params) {
      this.label = label;
      this.params = params;
    }

    public Error map() {
      return new Error(this.label, this.params);
    }
  }

  //endregion Public static types

  //region Public static methods
  public static BadPropertyError min(String property, Number val, Number min) {
    return new BadPropertiesClientException.BadPropertyError(
        clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-min"),
        Map.of("property", property, "val", val, "min", min));
  }

  public static BadPropertyError mustBeNotNull(String property) {
    return new BadPropertyError(
        clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-must-be-not-null"),
        Map.of("property", property));
  }

  public static BadPropertyError mustBeNull(String property) {
    return new BadPropertyError(
        clientRequestErrorBaseLabel("bad-request" + '.' + "property-violates-must-be-null-null"),
        Map.of("property", property));
  }
  //endregion Public static methods

  //region Private static properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  public static final Code CODE = new Code("FBTYV");
  //endregion Private static properties

  //region Constructors
  public BadPropertiesClientException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final BadPropertyError error, BadPropertyError... errors) {
    super(cause, CODE, id, Arrays.stream(errors).map(BadPropertyError::map).collect(() -> {
      final ArrayList<Error> l = new ArrayList<>(errors.length + 1);
      l.add(error.map());
      return l;
    }, ArrayList::add, ArrayList::addAll));
  }

  private BadPropertiesClientException(@NonNull final List<Error> errors) {
    super(null, CODE, new Id("0"), errors);
  }
  //endregion Constructors

}
