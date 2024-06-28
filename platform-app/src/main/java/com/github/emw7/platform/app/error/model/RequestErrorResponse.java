package com.github.emw7.platform.app.error.model;

import com.github.emw7.platform.error.RequestErrorException.Error;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * The structure of an app error response.
 *
 * @param timestamp date time when error occurred (was managed actually)
 * @param type      error type (CLIENT, SERVER, ...)
 * @param status    numerical error code
 * @param ref       REFeference, a unique identifier of the error
 * @param traceId   trace id observability tracing information
 * @param spanId    span id observability tracing information
 * @param message   the error message ({@code translate(language, label, params)})
 * @param label     the error label for translation
 * @param params    the error parameters to be put in the translation
 * @param errors    the list of detailed error occurred.
 */
public record RequestErrorResponse(ZonedDateTime timestamp,

                                   String type,
                                   int status,
                                   String ref,

                                   String traceId,
                                   String spanId,

                                   String message,
                                   String label,
                                   Map<String, Object> params,

                                   List<Error> errors) {

}
