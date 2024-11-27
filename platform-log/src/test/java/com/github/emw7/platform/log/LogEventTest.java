package com.github.emw7.platform.log;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventTest {

  private static LoggerConfig rootLoggerConfig = null;

  private final Logger log= LoggerFactory.getLogger(LogEventTest.class);

  private ListAppender testAppender = null;


  @BeforeAll
  public static void setUp() {

    System.setProperty("com.github.emw7.platform.log.trace-enabled","false");

    LoggerContext loggerContext = LoggerContext.getContext(false);

    Configuration configuration = loggerContext.getConfiguration();
    rootLoggerConfig = configuration.getLoggerConfig("");
  }

  @BeforeEach
  public void beforeTest() {
    testAppender = new ListAppender("testAppender");
    rootLoggerConfig.addAppender(testAppender, Level.ALL, null);
    testAppender.start();
  }

  @AfterEach
  public void afterTest() {
    testAppender.stop();
    rootLoggerConfig.removeAppender("testAppender");
  }

  @Test
  public void test_notice_default () {
    EventLogger.notice(log, "This is a {} notice", "DEFAULT").arg("defcon",99).log();

    final List<org.apache.logging.log4j.core.LogEvent> log = testAppender.getEvents();
    org.apache.logging.log4j.core.LogEvent logEntry = null;

    //noinspection SequencedCollectionMethodCanBeUsed
    logEntry = log.get(0);

    Assertions.assertThat(logEntry.getMessage().getFormattedMessage()).isEqualTo("[#event:notice][#what:This is a DEFAULT notice][#arg:defcon=99]");
    Assertions.assertThat(logEntry.getLevel().name()).isEqualTo("INFO");
  }

  @Test
  public void test_notice_warn () {
    EventLogger.notice(log, "This is a {} notice", "WARN").warn().arg("defcon",2).log();

    final List<org.apache.logging.log4j.core.LogEvent> log = testAppender.getEvents();
    org.apache.logging.log4j.core.LogEvent logEntry = null;

    //noinspection SequencedCollectionMethodCanBeUsed
    logEntry = log.get(0);

    Assertions.assertThat(logEntry.getMessage().getFormattedMessage()).isEqualTo("[#event:notice][#what:This is a WARN notice][#arg:defcon=2]");
    Assertions.assertThat(logEntry.getLevel().name()).isEqualTo("WARN");
  }

  @Test
  public void test_ctx () {
    try ( final LogContext logContext= EventLogger.context(Arg.of("ctx-1",1)) ) {
      DoingLogEvent doing= EventLogger.doing(log, "something within context").error().arg("act","do").log();
      //do something...
      // this arg("act",...) has no effects as an arg with the same name was already defined by doing and args
      //  is a Set and Set::add documentation states "Adds the specified element to this set if it
      //  is not already present (optional operation)."
      EventLogger.done(doing).warn().arg("do","end").arg("act","done").log();
    }
    EventLogger.notice(log, "This is a {} notice", "DEBUG").debug().arg("defcon",4).log();

    final List<org.apache.logging.log4j.core.LogEvent> log = testAppender.getEvents();
    org.apache.logging.log4j.core.LogEvent logEntry = null;

    //noinspection SequencedCollectionMethodCanBeUsed
    logEntry = log.get(0);
    Assertions.assertThat(logEntry.getMessage().getFormattedMessage())
        .matches("\\[#event:doing\\]"
            + "\\[#uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\]"
            + "\\[#what:something within context\\]"
            + "\\[#arg:ctx-1=1\\]"
            + "\\[#arg:act=do\\]");
    Assertions.assertThat(logEntry.getLevel().name()).isEqualTo("ERROR");

    logEntry = log.get(1);
    Assertions.assertThat(logEntry.getMessage().getFormattedMessage())
        .matches("\\[#event:done\\]"
            + "\\[#uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\]"
            + "\\[#what:something within context\\]"
            + "\\[#result:void\\]"
            + "\\[#arg:ctx-1=1\\]"
            + "\\[#arg:act=do\\]"
            + "\\[#arg:do=end\\]");
    Assertions.assertThat(logEntry.getLevel().name()).isEqualTo("ERROR");

    logEntry = log.get(2);
    Assertions.assertThat(logEntry.getMessage().getFormattedMessage())
        .isEqualTo("[#event:notice]"
            + "[#what:This is a DEBUG notice]"
            + "[#arg:defcon=4]");
    Assertions.assertThat(logEntry.getLevel().name()).isEqualTo("DEBUG");
  }

  @Test
  public void test_thread() throws InterruptedException {
    final CountDownLatch synch = new CountDownLatch(2);

    final Runnable r = () -> {
      EventLogger.notice(log, "this is thread {}", Thread.currentThread().getName())
          .arg("t", Thread.currentThread().getName()).log();
      synch.countDown();
    };

    try (final LogContext logContext = EventLogger.context(Arg.of("ctx-1", 1))) {
      DoingLogEvent doing = EventLogger.doing(log, "something within context").error()
          .arg("act", "do").log();
      //do something...
      new Thread(r, "thread-a").start();
      // this arg("act",...) has no effects as an arg with the same name was already defined by doing and args
      //  is a Set and Set::add documentation states "Adds the specified element to this set if it
      //  is not already present (optional operation)."
      EventLogger.done(doing).warn().arg("do", "end").arg("act", "done").log();
    }
    new Thread(r, "thread-b").start();
    EventLogger.notice(log, "This is a {} notice", "DEBUG").debug().arg("defcon", 4).log();

    final List<org.apache.logging.log4j.core.LogEvent> log = testAppender.getEvents();

    synch.await();

    log.forEach(event -> {
      if (event.getThreadName().equals("thread-a")) {
        Assertions.assertThat(event.getMessage().getFormattedMessage())
            .isEqualTo("[#event:notice][#what:this is thread thread-a][#arg:t=thread-a]");
        Assertions.assertThat(event.getLevel().name()).isEqualTo("INFO");
      } else if (event.getThreadName().equals("thread-b")) {
        //
        Assertions.assertThat(event.getMessage().getFormattedMessage())
            .isEqualTo("[#event:notice][#what:this is thread thread-b][#arg:t=thread-b]");
        Assertions.assertThat(event.getLevel().name()).isEqualTo("INFO");
      }
    });
  }

}
