package com.github.emw7.examples.serviceruntime.logic.error.client;

import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestError(errorCode = 423, label = "app.i18n.error.client.locked")
public @interface LockedClientError {

}
