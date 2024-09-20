package com.github.emw7.platform.i18n;

import com.github.emw7.platform.log.EventLogger;
import jdk.jfr.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @see <a href="https://sultanov.dev/blog/access-spring-beans-from-unmanaged-objects/">Section '4.
 * OPTION 2: IMPLEMENT PROVIDER' of Access Spring Beans from Unmanaged Objects</a>
 */
public final class TranslatorContainer {

  //region Private static final properties
  private static final Logger log = LoggerFactory.getLogger(TranslatorContainer.class);
  //endregion Private static final properties

  //region Private static properties
  /*
     DESIGN

     Q: Why is it volatile?
     A: https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
   */
  private static volatile Translator translator;
  //endregion Private static properties

  //region Private static methods
  private static void logIgnoring (@Nullable final Translator translator)
  {
    EventLogger.notice(log)
        .warn()
        .pattern("ignoring instance {} as translator already set with instance {}")
        .params(( translator == null ) ? "null" : translator, getTranslator())
        .log();
  }
  //endregion Public static methods

  //region Public static methods

  /**
   * Returns either {@link #translator} ot {@link FooTranslator} if {@link #translator} is
   * {@code null}.
   * <p>
   * <b>Note</b>: to avoid application to face with {@code null} returns a default
   * translator implementation in case {@link #translator} has not been set.
   *
   * @return either {@link #translator} ot {@link FooTranslator} if {@link #translator} is
   * {@code null}
   */
  // returns null is called before any object instantiation
  public static @NonNull Translator getTranslator() {
    if (translator == null) {
      EventLogger.notice(log).warn().pattern(
              "{}#getTranslator() called but translator is null, so returning an instance of {}")
          .params(TranslatorContainer.class.getName(), FooTranslator.class.getName()).log();
      return new FooTranslator();
    } else {
      return translator;
    }
  }
  //endregion Public static methods

  //region Constructors

  /**
   * <b>Note</b>: <b>MUST NOT</b> be used by an application, as it
   * is constructed by {@link com.github.emw7.platform.i18n.autoconfig.PlatformI18nAutoConfig}.<br/>
   * This is {@code public} only to be used in tests.
   * <p>
   * Sets {@link #translator} with the supplied translator if it is not {@code null}.
   *
   * @param translator translator to which set {@link #translator} if it is not {@code null}.
   */
  public TranslatorContainer(@NonNull final Translator translator) {
    if (TranslatorContainer.translator == null) {
      synchronized (TranslatorContainer.class) {
        if (TranslatorContainer.translator == null) {
          TranslatorContainer.translator = translator;
        }
        else {
          logIgnoring(translator);
        }
      }
    }
    else {
      logIgnoring(translator);
    }
  }
  //endregion Constructors

}
