package com.github.emw7.platform.log.examples.reload4j;

import static com.github.emw7.platform.log.EventLogger.caught;
import static com.github.emw7.platform.log.EventLogger.context;
import static com.github.emw7.platform.log.EventLogger.doing;
import static com.github.emw7.platform.log.EventLogger.done;
import static com.github.emw7.platform.log.EventLogger.notice;
import static com.github.emw7.platform.log.EventLogger.throwing;

import com.github.emw7.platform.log.Arg;
import com.github.emw7.platform.log.DoingLogEvent;
import com.github.emw7.platform.log.LogContext;
import com.github.emw7.platform.log.LogEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


public class PlatformLogApp {

  private static final Logger log = LoggerFactory.getLogger(PlatformLogApp.class);

  public static void main(String[] argv) {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    long log_rows= 5_000_000;
    if ( argv.length >= 1 ) {
      try {
        log_rows = Long.parseLong(argv[0]);
      } catch ( NumberFormatException e ) {
        System.err.printf("argv[0]=%s%n",argv[0]);
        log_rows= 0;
      }
    }

    System.err.printf("[%s] Start...%d%n", formatter.format(LocalDateTime.now()), log_rows);

    final long ts = System.nanoTime();

    try (LogContext context = context(Arg.of("ex", "ex-v"))) {
      aMethod(context);
    }

    for ( int i = 0 ; i < log_rows; i++ ) {
      notice(log, "notice").arg("i", i).log();
    }

    notice(log, "fourth notice, who knows about ctx args").log();

    throwing(log, new Exception("ahia", new NullPointerException("this is the cause")),
        "throwing exception").log();

    caught(log, new Exception("un'altra eccezione", new NullPointerException()),
        "caught exception").arg("oh","god").log();

    final long te = System.nanoTime();

    System.err.printf("[%s] duration after 5E6 e few more log rows: %d milliseconds%n", formatter.format(LocalDateTime.now()), Duration.ofNanos(te-ts).toMillis());

    Thread.ofVirtual().factory().newThread( () -> {
      try {
        LogEvent.terminated.await();
        System.err.printf("[%s] ...End%n", formatter.format(LocalDateTime.now()));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }}).start();
  }

  private static void aMethod(@NonNull final LogContext context) {
    try {
      notice(log, "notice").log();
      notice(log, "second notice").log();
      context.addArg("post", "post-va");
      notice(log, "third notice should contain post ctx arg").log();
      final DoingLogEvent somethingLogEvent = doing(log, "something {}", "beautiful").arg("greeting", "hello").log();
      if (false) {
        throw new Exception("while doing... cannot done");
      }
      done(somethingLogEvent, 123).log();
      done(somethingLogEvent, null).log();
      done(somethingLogEvent).log();
    } catch (Exception e) {
      // oh, no!
      caught(log, e, "oh, no!, this is a {}", "mess").log();
    }
  }

}
