package com.github.emw7.platform.log;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class LogContext implements AutoCloseable {

  //region Private final properties
  private final Set<Arg<?>> args;
  //endregion Private final properties

  //region Constructors
  LogContext(@NonNull final Arg<?>... args) {
    // 3 is an arbitrary number: 3 has been individuated as the average number of argument for a
    //  a log context.
    this.args = new HashSet<>(args.length == 0 ? 3 : args.length);
    Stream.of(args).forEach(
        arg -> {
          MDC.put(arg.name(), arg.asString());
          this.args.add(arg);
        });
  }
  //endregion Constructors

  //region API

  /**
   * Adds an arg to the log context.
   * <p>
   * Args added to the log context are printed together with the messages logged within the log
   * context and are removed ({@link #close()}) the log context is closed if used within a
   * try-with-resources statement.
   *
   * @param name  name of the arg
   * @param value value of the arg
   * @param <T>   type of the arg's value
   * @return {@code this}
   */
  @SuppressWarnings("UnusedReturnValue")
  public <T> LogContext addArg(@NonNull String name, @Nullable final T value) {
    return addArg(Arg.of(name, value));
  }

  /**
   * Behaves exactly as {@link #addArg(String, Object)} but working with an {@link Arg} instead of a
   * (name, value) pair.
   *
   * @param arg the arg to be added to the log context
   * @param <T> type of the arg's value
   * @return {@code this}
   */
  public <T> LogContext addArg(@NonNull final Arg<T> arg) {
    MDC.put(arg.name(), arg.asString());
    args.add(arg);
    return this;
  }

  /**
   * Removes from the log context the arg with the specified name.
   *
   * @param name name of the arg to remove
   * @param <T>  type of the arg's value
   * @return {@code this}
   */
  public <T> LogContext remArg(@NonNull final String name) {
    MDC.remove(name);
    args.remove(Arg.of(name, null));
    return this;
  }

  /**
   * Removes all args added to this log context.
   */
  @Override
  public void close() {
    args.stream().map(Arg::name).forEach(MDC::remove);
  }
  //endregion API

}
