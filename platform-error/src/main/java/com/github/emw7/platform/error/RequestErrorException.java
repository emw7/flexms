package com.github.emw7.platform.error;

import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.i18n.TranslatorContainer;
import com.github.emw7.platform.telemetry.tracing.TracingContainer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

// WARNING TEST: it is not easy to test because uses static/singleton TranslationContainer.

/**
 * Represent any error occurred during request elaboration.
 */
public abstract sealed class RequestErrorException extends Exception permits
    ClientRequestErrorException,
    ServerRequestErrorException {

  //region Public static methods
  public static @NonNull String appClientErrorLabel(@NonNull final String error) {
    return "app.error.client." + error;
  }

  public static @NonNull String appServerErrorLabel(@NonNull final String error) {
    return "app.error.server." + error;
  }

  /**
   * Returns an unmodifiable map containing all entries of {@code params} plus the enrichment.
   * <p>
   * Elements in {@code enrichment} are managed as pairs of (key, values):
   * <pre>
   *   enrichment : a, 1, b, c, d, 3.14
   *   then to params the following entries are added:
   *     (a, 1); (b, c); (d, 3.14)
   * </pre>
   * If {@code enrichment} contains odd elements the last is discarded:
   * <pre>
   *   enrichment : a, 1, b, c, d
   *   then to params the following entries are added (note that d is discarded and it has not
   *   the corresponding value):
   *     (a, 1); (b, c)
   * </pre>
   * {@code enrichment} overwrites entries in {@code params}:
   * <pre>
   *   params : (b, x); (d, 3.14)
   *   enrichment : a, 1, b, c
   *   then the following map is returned:
   *     (a, 1); (b, c); (d, 3.14)
   * </pre>
   *
   * @param params     parameters to be enriched
   * @param enrichment keys and values to be added to params
   * @return an unmodifiable map containing all entries of {@code params} plus the enrichment
   */
  public static @NonNull /*unmodifiable*/ Map<String, Object> enrichParams(
      @Nullable Map<String, Object> params, @NonNull final Object... enrichment) {
    final Map<String, Object> enrichedParams = new HashMap<>(
        Optional.ofNullable(params).orElse(Map.of()));
    enum T {KEY, VALUE};
    T t = T.KEY;
    String key = null;
    for (Object o : enrichment) {
      t = switch (t) {
        case KEY -> {
          key = Optional.ofNullable(o).map($ -> ($ instanceof String) ? (String) $ : $.toString())
              .orElse(null);
          yield T.VALUE;
        }
        case VALUE -> {
          enrichedParams.put(key, o);
          yield T.KEY;
        }
      };
    }
    return Collections.unmodifiableMap(enrichedParams);
  }
  //endregion Public static methods

  //region Private static methods

  /**
   * If no-errors OR 2+ errors then buildRef else buildMessage(error,code,id)
   *
   * @param errors
   * @param code
   * @param id
   * @return
   */
  private static String buildMessage(@Nullable final List<Error> errors, @NonNull final Code code,
      @NonNull final Id id) {
    if (CollectionUtils.isEmpty(errors) || errors.size() >= 2 || errors.getFirst() == null) {
      return buildRef(code, id);
    }
    // else... errors has exactly 1 element, and it is *NOT* null (see errors.getFirst() == null
    //  above.
    final Error error = errors.getFirst();
    // it is not true that errors.params() can be null as Error forces params to be empty map in case
    //  null is passed.
    return buildMessage(error.message(), error.label(), error.params(), code, id);
  }

  /**
   * If message NOT null (already translated?) then {@code <ref> <translate(label)>}.
   *
   * @param message
   * @param label
   * @param params
   * @param code
   * @param id
   * @return
   */
  private static @NonNull String buildMessage(@Nullable final String message,
      @NonNull final String label, @NonNull final Map<String, Object> params,
      @NonNull final Code code, @NonNull final Id id) {
    final String usedMessage;
    if (message != null) {
      usedMessage = message;
    } else {
      usedMessage = TranslatorContainer.getTranslator().translate((Locale)null, label, params);
    }
    return String.format("%s @%s", buildRef(code, id), usedMessage);
  }

  /**
   * Returns the REFerence built from specified {@code code} and {@code id}.
   *
   * @param code
   * @param id
   * @return the REFerence built from specified {@code code} and {@code id}
   */
  private static String buildRef(@NonNull final Code code, @NonNull final Id id) {
    return String.format("%s-%s", code, id);
  }

//  private static String getTraceId() {
//    return getId(Tracing::traceId);
//  }
//
//  private static String getSpanId() {
//    return getId(Tracing::spanId);
//  }
//
//  private static String getId(Function<Tracing, String> f) {
//    return Optional.ofNullable(TracingContainer.get()).map(f).orElse(null);
//  }


  private static @Nullable /*unmodifiable*/ List<Error> translate(
      @Nullable final List<Error> errors) {
    final Translator translator = TranslatorContainer.getTranslator();
    if (translator == null) {
      return errors;
    }
    /// else... translator != null

    if (errors == null) {
      return null;
    }
    // else... errors != null
    return errors.stream().map(RequestErrorException::errorWithMessage).toList();
  }

  private static @Nullable Error errorWithMessage(@Nullable final Error error) {
    if (error == null) {
      return null;
    } else if (error.message() != null) {
      // error message is not null... use it and do not set from label translation.
      return error;
    } else {
      return new Error(TranslatorContainer.getTranslator().translate((Locale)null, error.label(), error.params()),
          error.label(), error.params());
    }
  }
  //endregion Private static methods

  //region Public types

  /**
   * A request error.
   * <p>
   * {@code error} should be obtained with something like this: {@code translate(<language>,label,params)}.
   * <pre>
   *   label = app.error.severe
   *   params = {"severity":1}
   *   translation of app.error.sever in english is <b>Severe error occurred: {severity}}</b>
   *   then error should be = translate("en","app.error.severe",{"severity":1}) = <b>Severe error occurred: 1</b>
   * </pre>
   *
   * @param message the translation of {@code label}
   * @param label the label with which retrieve the error message translation
   * @param params the params to be used actual values in the label placeholders; if {@code null} then it is forced to an unmodifiable empty map
   */
  // TODO rename label in i18nLabel
  public record Error(@Nullable String message, @NonNull String label,
                      @Nullable Map<String, Object> params) {

    // compact constructor

    /**
     * Forces params to be an unmodifiable empty map if passed value is {@code null}.
     *
     * @param message
     * @param label
     * @param params  if {@code null} then it is passed set to an unmodifiable empty map
     */
    public Error {
      params = (params == null) ? Map.of() : params;
    }

    // this constructor passes through canonical constructor that forces params to be empty map
    //  if specified value is null; can be verified witht he following test:
    //  org.assertj.core.api.Assertions.assertThat(new RequestErrorException.Error("label", null).params()).as("null params forced to empty map").isNotNull().isEmpty()
    public Error(@NonNull String label, @Nullable Map<String, Object> params) {
      this(null, label, params);
    }
  }
  //endregion Public types

  //region Properties

  private final String type;

  /**
   * The error code.
   */
  private final Code code;
  private final Id id;

  private final List<Error> errors;

  private final String traceId;
  private final String spanId;
  //endregion Properties

  //region Constructors
  protected RequestErrorException(@Nullable final Throwable cause, @NonNull final String type,
      @NonNull final Code code, @NonNull final Id id, @Nullable final List<Error> errors) {
    super(buildMessage(errors, code, id), cause);
    this.type = type;
    this.code = code;
    this.id = id;
    this.errors = translate(errors);
    this.traceId = TracingContainer.traceId();
    this.spanId = TracingContainer.spanId();
  }

  protected RequestErrorException(@NonNull final String type, @NonNull final Code code,
      @NonNull final Id id, @Nullable final List<Error> errors) {
    this(null, type, code, id, errors);
  }

  protected RequestErrorException(@Nullable final Throwable cause, @NonNull final String type,
      @NonNull final Code code, @NonNull final Id id, @NonNull final Error error) {
    this(cause, type, code, id, List.of(error));
  }

  protected RequestErrorException(@NonNull final String type, @NonNull final Code code,
      @NonNull final Id id, @NonNull final Error error) {
    this(null, type, code, id, error);
  }
  //endregion Constructors

  //region Getters & Setters
  public final String getType() {
    return type;
  }

  public final String getRef() {
    return code.toString() + "-" + id.toString();
  }

//  public final @NonNull String getId() {
//    return id.toString();
//  }

  public final @NonNull /*unmodifiable*/ List<Error> getErrors() {
    return Collections.unmodifiableList(Optional.ofNullable(errors).orElse(List.of()));
  }

  //endregion Getters & Setters

}
