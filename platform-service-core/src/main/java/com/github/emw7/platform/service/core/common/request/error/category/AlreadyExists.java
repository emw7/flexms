package com.github.emw7.platform.service.core.common.request.error.category;

import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.core.annotation.AliasFor;

/**
 * The already-exists client error category, to be used when an entity that has been requested to be
 * created already exists.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@I18nLabel(params={})
@RequestError(errorCode = 409, label = "com.github.emw7.platform.i18n.error.request.client.already-exists")
public @interface AlreadyExists {

// TODO remove commented out code.
//  //  /**
////   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
////   * {@link ClientExceptionHandler}.
////   */
//  @AliasFor(annotation = RequestError.class, attribute = "label")
//  String label() default "";
//
//  String params() default "";
//  // end ATTENTION.
}
