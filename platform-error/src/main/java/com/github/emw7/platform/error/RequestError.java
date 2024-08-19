package com.github.emw7.platform.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Not actually a type but a sort of parent of the actual types.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestError {

  int errorCode();

  String label();// default Constants.DEFAULT_ERROR_LABEL;
}
