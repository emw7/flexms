package com.github.emw7.flexms.platform.error.core;

import com.github.emw7.flexms.platform.telemetry.TracingContainer;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Represent any error occurred during request elaboration.
 */
public abstract sealed class RequestErrorException extends Exception permits
    ClientRequestErrorException, ServerRequestErrorException {

  private static String format(@NonNull final String token, final int size) {
    return StringUtils.leftPad(token, size, '0');
  }

  private static String formatType(@NonNull final String type) {
    return format(type, 1);
  }

  private static String formatCode(@NonNull final String category) {
    return format(category, 5);
  }
  //region Properties

  /**
   * The error message in (any) language.
   * <p>
   * For example could be {@code translate("en",label}. The label with which retrieve the error
   * message translation.
   * <p>
   * For example {@link #message} could be {@code translate("en",label}.
   */
  public record Error(String message, String label, Map<String, String> params) {

  }

  private final String type;

  /**
   * The error code.
   */
  private final String code;

  private final List<Error> errors;

//  /**
//   * The error description in (any) language.
//   * <p>
//   * For example could be {@code translate("en",label}.
//   *
//   * @see #label
//   */
//  private final String description;
//
//  /**
//   * The label with which retrieve the error description translation.
//   * <p>
//   * For example {@link #description} could be {@code translate("en",label}.
//   */
//  private final String label;
  //endregion Properties

  //region Constructors
  protected RequestErrorException(@NonNull final String type, @NonNull final String typeString,
      @NonNull final String code, @Nullable final List<Error> errors) {
    super(buildCode(type, code));
    this.type = typeString;
    this.code = this.getMessage();
    this.errors = errors;
  }
  //endregion Constructors


  //region Getters & Setters
  public String getType() {
    return type;
  }

  public String getCode() {
    return code;
  }

  public List<Error> getErrors() {
    return errors;
  }

  //endregion Getters & Setters

  //region Private methods
  private static String buildCode(@NonNull final String type, @NonNull final String code) {
    final String traceId = TracingContainer.get().traceId();
    final String spanId = TracingContainer.get().spanId();
    return String.format("%s-%s:%s/%s", formatType(type), formatCode(code), traceId, spanId);
  }
  //endregion Private methods

}
