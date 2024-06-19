package com.github.emw7.platform.error.category;

import com.github.emw7.platform.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 409, label = "app.error.request.already-exists")
public @interface AlreadyExists {

  //  /**
//   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
//   * {@link ClientExceptionHandler}.
//   */
  String label() default "";

  String params() default "";
  // end ATTENTION.
}
