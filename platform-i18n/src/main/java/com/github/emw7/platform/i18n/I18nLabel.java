package com.github.emw7.platform.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO [xwr]:
//  potrebbe essere che si rimuove la costante e si togli anche I18nLabel e si mettono valori
//  hardcodati ovunque... cmq se cambio la costante devo cambiare anche nei vari .properties...
//  per cui forse vale la pena hardcodare... vero è che I18nLabel mi aiutava a cercare le label
//  per vedere se ne avevo dimenticate nei .properties... e a tracciare i params... che altrimenti
//  vanno cercati nei .properties
@Retention(RetentionPolicy.CLASS) // NOT used at runtime.
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface I18nLabel {

  String label();
  String[] params() default {};


}
