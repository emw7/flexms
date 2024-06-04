package com.github.emw7.flexms.platform.error.core;

import com.github.emw7.flexms.platform.error.core.RequestErrorException.Error;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * The structure of a client error response.
 *
 * @param timestamp  date time when error occurred (was managed actually)
 * @param type       error type
 * @param httpStatus http status code (https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)
 * @param code       unique error code (see TODO)
 * @param label      TODO
 * @param params     TODO
 * @param errors     TODO
 */
public record RequestErrorResponse(ZonedDateTime timestamp, String type, int httpStatus,
                                   String code, String label, Map<String, Object> params,
                                   List<Error> errors) {

}
