package com.github.emw7.platform.error.category;

import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 400, label = Constants.I18N_LABEL_REQUEST_PREFIX + "bad-request")
public @interface BadRequest {

  //  /**
//   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
//   * {@link ClientExceptionHandler}.
//   */
  String label() default "";

  // TODO doc.
  String params() default "";
  // end ATTENTION.
}
