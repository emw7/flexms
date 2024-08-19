package com.github.emw7.platform.error.category;

import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 404, label = Constants.I18N_LABEL_REQUEST_PREFIX + "not-found")
public @interface NotFound {

  //  /**
//   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
//   * {@link ClientExceptionHandler}.
//   */
  String label() default "";

  String params() default "";
  // end ATTENTION.
}
