package com.github.emw7.platform.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS) // NOT used at runtime.
@Target(ElementType.FIELD)
@Documented
public @interface I18nLabel {

  String label();
  String[] params() default {};


}
