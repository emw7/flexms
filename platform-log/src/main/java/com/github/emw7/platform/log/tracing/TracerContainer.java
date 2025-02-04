package com.github.emw7.platform.log.tracing;

import io.micrometer.tracing.Tracer;
import org.springframework.lang.NonNull;

public final class TracerContainer {

  //region Private static properties
  /*
     DESIGN

     Q: Why is it volatile?
     A: https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
   */
  private static volatile Tracer tracer;
  //endregion Private static properties

  //region Private static methods
  //endregion Public static methods

  //region Public static methods

  /**
   * Returns either {@link #tracer} ot {@link Tracer#NOOP} if {@link #tracer} is
   * {@code null}.
   * <p>
   * <b>Note</b>: to avoid application to face with {@code null} returns a default
   * tracer implementation in case {@link #tracer} has not been set.
   *
   * @return either {@link #tracer} ot {@link Tracer#NOOP} if {@link #tracer} is
   * {@code null}
   */
  public static @NonNull Tracer getTracer() {
    if (tracer == null) {
      System.err.printf("[WARN] %s#getTracer() called but tracer is null, so returning a fallback instance of %s%n", TracerContainer.class.getName(), Tracer.NOOP.getClass().getName());
      return Tracer.NOOP;
    } else {
      return tracer;
    }
  }
  //endregion Public static methods

  //region Constructors

  /**
   * <b>Note</b>: <b>MUST NOT</b> be used by an application, as it
   * is constructed by {@link com.github.emw7.platform.log.autoconfig.PlatformLogTraceAutoConfig}.<br/>
   * This is {@code public} only to be used in tests.
   * <p>
   * Sets {@link #tracer} with the supplied tracer if it is not {@code null}.
   * If {@link #tracer} is not {@code null} then a {@code warn} log is printed,
   * and the supplied tracer is ignored.
   *
   * @param tracer tracer to which set {@link #tracer} if it is not {@code null}.
   */
  public TracerContainer(@NonNull final Tracer tracer) {
    if (TracerContainer.tracer == null) {
      synchronized (TracerContainer.class) {
        if (TracerContainer.tracer == null) {
          TracerContainer.tracer = tracer;
        }
      }
    }
  }
  //endregion Constructors

}
