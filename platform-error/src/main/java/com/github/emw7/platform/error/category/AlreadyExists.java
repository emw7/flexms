package com.github.emw7.platform.error.category;

import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.i18n.I18nLabel;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
// TODO add I18nLabel and [xwr] ha senso mettere la costante che poi in I18nLabel metto tutto il valore?:
//  se cambio il valore della costante devo cambiare anche tutti gli I18nLabel!!!
@RequestError(errorCode = 409, label = Constants.I18N_LABEL_REQUEST_PREFIX + "already-exists")
public @interface AlreadyExists {

  //  /**
//   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
//   * {@link ClientExceptionHandler}.
//   */
  String label() default "";

  String params() default "";
  // end ATTENTION.
}
