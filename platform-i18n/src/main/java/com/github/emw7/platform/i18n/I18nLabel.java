package com.github.emw7.platform.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
   DESIGN

   Initially, the field label() was also added to I18nLabel (which was a full string),
   and the value of the constant was composed of multiple constants. The purpose of label()
   was to allow searching for labels in the .properties files from the code and vice versa.
   The use of constants to compose the value of the constant was meant to facilitate changes
   to the label values, but this was only true for the code. In fact, in the .properties files
   and in label(), a search & replace was still required, which was unrelated to the change of
   one or more constants' values, thus adding room for error.

   It was therefore decided to remove label() and not use constants, but instead put the value
   directly in the I18N constant (annotated by @I18nLabel), in order to have only two repetitions
   of the label (in the .properties file) and as the value of the I18N constant. In case the
   value changes, only one search & replace will be needed.

   Note: This discussion is about .properties files, but it’s true that the association between
   labels and messages (plural because of the different languages) could be located elsewhere
   (in a database, for example). However, for the EMW7 platform, the association is within the
   .properties files that come with the EMW7 platform projects.
 */

/**
 * Ideally to be used with {@code static final String} properties to annotate that the constant
 * represents an I18N label (aka code).
 * <p>
 * The {@link #params()} field is the list of parameters the message, that the label represents,
 * accepts.
 * <p>
 * Example-A
 * <pre>
 * public class Foo {\
 *   @I18nLabel(params = {status})
 *   public static final String I18N_LABEL = "foo.i18.message"
 * }
 *
 * messages.properties:
 *   foo.i18n.message=The status of foo is {status}
 * </pre>
 */
@Retention(RetentionPolicy.CLASS) // NOT used at runtime.
@Target({ElementType.FIELD})
@Documented
public @interface I18nLabel {

  String[] params();

}
