package com.github.emw7.platform.service.core.common.request.error.category;

import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 400, label = "com.github.emw7.platform.i18n.error.request.client.bad-request")
public @interface BadRequest {

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
