package com.github.emw7.platform.core.lang;

public final class InterruptedExceptionUtil {

  /**
   * From <a href="https://www.baeldung.com/java-interrupted-exception">How to Handle
   * InterruptedException in Java</a> section
   * <a href="https://www.baeldung.com/java-interrupted-exception#2-restore-the-interrupt">4.2.
   * Restore the Interrupt</a>:<br/>
   * There are some cases where we can’t propagate InterruptedException.
   * For example, suppose our task is defined by a Runnable or overriding a method that doesn’t
   * throw any checked exceptions. In such cases, we can preserve the interruption. The standard way
   * to do this is to restore the interrupted status.
   * <pre>
   * try {
   *   // code that can cause InterruptedException...
   * } catch (InterruptedException e) {
   *   InterruptedExceptionUtil.manage()
   * }
   * </pre>
   */
  public static void restoreTheInterrupt() {

    Thread.currentThread().interrupt();
  }
}
