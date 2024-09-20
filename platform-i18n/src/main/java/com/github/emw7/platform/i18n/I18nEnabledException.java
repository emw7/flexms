package com.github.emw7.platform.i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base for checked exception that needs/wants the exception message got from {@link Translator}.
 * <p>
 * This class exposes all the constructors from {@link Exception} that has the message as argument
 * replacing it with {@code label} and {@code params}.<br/>
 * The message is set as the string obtained by translating the provided label with the provided
 * parameter to which the {@code cause} message has been added with the key {@code errorReason}.
 * <p>
 * What specified above means that the message associated with the label can use all the parameters
 * specified in {@code params} argument plus {@code errorReason}.
 * <p>
 * <b>Example-A</b>
 * <pre>
 * public final class AcmeI18nException extends I18nEnabledException {
 *   private static final String LABEL = "exception.acme.message";
 *   public AcmeI18nException (String where, Throwable cause) {
 *     super(LABEL, Maps.of("where",where),cause);
 *   }
 * }
 *
 * resources:
 *   messages.properties:
 *     exception.acme.message = an error occurred at {where} caused by {errorReason}
 *
 * // results in an exception which message is:
 * //  an error occurred at here caused by a curious exception.
 * throw new AcmeI18nException("here",new Exception("a curious exception"));
 * </pre>
 * <p>
 * <b>Notes</b>:
 * <ol>
 * <li>The locale used for translation is delegated to actual {@link Translator} instance.</li>
 * <li>If {@code cause} is {@code null} then the {@code errorReason} is set to the translations of
 * the {@value #I18N_LABEL_UNKNOWN_ERROR_REASON}.</li>
 * <li>Because of an exception is built with {@code new} it cannot access the beans it uses
 * {@link TranslatorContainer} to get the translator bean instance.</li>
 * </ol>
 */
public abstract class I18nEnabledException extends Exception {

  //region Private static properties
  @I18nLabel(params = {})
  private static final String I18N_LABEL_UNKNOWN_ERROR_REASON = "com.github.emw7.platform.i18n.unknown-error-reason";
  //endregion Private static properties

  //region Private static methods

  /**
   * Adds error reason to {@code params} as the message of the specified cause.
   * <p>
   * The error reason is an entry which key is {@code errorReason} and value is the localized
   * message of the specified {@code cause}; if {@code cause} is null then the error reason is set
   * as the translation of the {@value #I18N_LABEL_UNKNOWN_ERROR_REASON} label.
   * <p>
   * If {@code params} is {@code null} then a map with only error reason is returned.
   *
   * @param params the parameters to which add the error reason.
   * @param cause  the exception cause
   * @return the parameters with error reason entry added.
   */
  private static @NonNull Map<String, Object> addErrorReason(
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    final String errorReason = (cause == null) ? TranslatorContainer.getTranslator()
        .translate((Locale) null, I18N_LABEL_UNKNOWN_ERROR_REASON, null)
        : String.valueOf(cause.getLocalizedMessage());
    if (params == null) {
      return Map.of("errorReason", errorReason);
    } else {
      final Map<String, Object> paramsWithErrorReason = new HashMap<>(params);
      paramsWithErrorReason.put("errorReason", errorReason);
      return Collections.unmodifiableMap(paramsWithErrorReason);
    }
  }

  /**
   * Returns the translation of the supplied label interpolated with the specified parameters to
   * which error reason ({@link #addErrorReason(Map, Throwable)} has been added.
   *
   * @param label  label for which retrieve the message
   * @param params parameters available for the interpolation in the message
   * @param cause  the exception cause from which retrieved the error reason
   * @return the translation of the supplied label interpolated with the specified parameters to
   * which error reason ({@link #addErrorReason(Map, Throwable)} has been added.
   */
  private static @NonNull String messageFromLabel(@NonNull final String label,
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    String msg= TranslatorContainer.getTranslator()
        .translate((Locale) null, label, addErrorReason(params, cause));
    return msg;
  }
  //endregion Private static methods

  //region Constructors

  /**
   * Builds an exception which message is the translation of label interpolated with {@code params}
   * and error reason.
   *
   * @param label              label for which retrieve the message
   * @param params             parameters available for the interpolation in the message
   * @param cause              the exception cause from which retrieved the error reason
   * @param enableSuppression  see {@link Exception#Exception(String, Throwable, boolean, boolean)}
   * @param writableStackTrace see {@link Exception#Exception(String, Throwable, boolean, boolean)}
   */
  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace) {
    super(messageFromLabel(label, params, cause), cause, enableSuppression, writableStackTrace);
  }

  /**
   * See {@link I18nEnabledException#I18nEnabledException(String, Map, Throwable, boolean, boolean)}
   * for a general description.
   *
   * @param label  label for which retrieve the message
   * @param params parameters available for the interpolation in the message
   * @param cause  the exception cause from which retrieved the error reason
   */
  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params, @Nullable final Throwable cause) {
    super(messageFromLabel(label, params, cause), cause);
  }

  /**
   * See {@link I18nEnabledException#I18nEnabledException(String, Map, Throwable, boolean, boolean)}
   * for a general description.
   *
   * @param label  label for which retrieve the message
   * @param params parameters available for the interpolation in the message
   */
  public I18nEnabledException(@NonNull final String label,
      @Nullable final Map<String, Object> params) {
    super(messageFromLabel(label, params, null));
  }
  //endregion Constructors

}
