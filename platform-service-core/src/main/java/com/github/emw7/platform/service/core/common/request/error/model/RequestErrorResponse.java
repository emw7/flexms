package com.github.emw7.platform.service.core.common.request.error.model;

import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.lang.NonNull;

/**
 * The structure of an app error response.
 *
 * @param timestamp date time when error occurred (was managed actually)
 * @param type      error type (CLIENT, SERVER, ...)
 * @param status    numerical error code
 * @param ref       reference, a unique identifier of the error
 * @param traceId   trace id observability tracing information
 * @param spanId    span id observability tracing information
 * @param message   the error message ({@code translate(language, label, params)})
 * @param label     the error label for translation
 * @param errors    the list of detailed error occurred.
 */
public record RequestErrorResponse(@NonNull ZonedDateTime timestamp,

                                   @NonNull String type,
                                   @NonNull int status,
                                   @NonNull String ref,

                                   @NonNull String traceId,
                                   @NonNull String spanId,

                                   @NonNull String message,
                                   @NonNull String label,

                                   @NonNull List<Error> errors) {

  public RequestErrorResponse {
    //noinspection ConstantValue
    errors= ( errors == null ) ? List.of() : errors;
  }
}
