package com.github.emw7.platform.service.core.common.request.error.category;

import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.core.annotation.AliasFor;

/**
 * The bad-request client error category, to be used when the request cannot be completed because
 * if is incorrect in some way.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@I18nLabel(params={})
@RequestError(errorCode = 400, label = "com.github.emw7.platform.i18n.error.request.client.bad-request")
public @interface BadRequest {

// TODO remove commented out code.
//  //  /**
////   * ATTENTION: do *NOT* rename as they are retrieved via name using reflection in
////   * {@link ClientExceptionHandler}.
////   */
//  @AliasFor(annotation = RequestError.class, attribute = "label")
//  String label() default "";
//
//  // TODO doc.
//  String params() default "";
//  // end ATTENTION.
}
