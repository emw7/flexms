package com.github.emw7.flexms.platform.error.core.category;

import com.github.emw7.flexms.platform.error.core.RequestError;
import com.github.emw7.flexms.platform.rest.ClientExceptionHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 404, label = "app.error.request.not-found")
public @interface NotFound {
  /**
   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
   * {@link ClientExceptionHandler}.
   */
  String label() default "";
  String params() default "";
  // end ATTENTION.
}
