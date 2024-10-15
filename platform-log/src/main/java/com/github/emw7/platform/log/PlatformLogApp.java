package com.github.emw7.platform.log;

import static com.github.emw7.platform.log.EventLogger.caught;
import static com.github.emw7.platform.log.EventLogger.context;
import static com.github.emw7.platform.log.EventLogger.doing;
import static com.github.emw7.platform.log.EventLogger.done;
import static com.github.emw7.platform.log.EventLogger.notice;
import static com.github.emw7.platform.log.EventLogger.throwing;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class PlatformLogApp {

  private static final Logger log = LoggerFactory.getLogger(PlatformLogApp.class);

  public static void main(String[] argv) {

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        LogEvent.terminated.await();
        LogManager.shutdown();
      } catch (InterruptedException e) {
        LogManager.shutdown();
        Thread.currentThread().interrupt();
      }
    }));

    try (LogContext context = context(Arg.of("ex", "ex-v"))) {
      xxx(context);
    }

    for ( int i = 0 ; i < 5_000_000; i++ ) {
      notice(log, "notice").arg("i", i).log();
    }
    notice(log, "fourth notice, who knows about ctx args").log();

    throwing(log, new Exception("ahia", new NullPointerException("this is the cause")),
        "throwing exception").log();

    caught(log, new Exception("un'altra eccezione", new NullPointerException()),
        "caught exception").arg("oh", "god").log();

//    try {
//      Thread.sleep(Duration.ofMillis(2));
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
  }

  private static void xxx(@NonNull final LogContext context) {
    try {
      notice(log, "notice").log();
      notice(log, "second notice").log();
      context.addArg("post", "post-va");
      notice(log, "third notice should contain post ctx arg").log();
      final DoingLogEvent somethingLogEvent = doing(log, "something {}", "beautiful").arg(
          "greeting", "hello").log();
      if (false) {
        throw new Exception("while doing... cannot done");
      }
      done(somethingLogEvent, 123).log();
      done(somethingLogEvent, null).log();
      done(somethingLogEvent).log();
    } catch (Exception e) {
      // oh, no!
      caught(log, e, "oh, no!, this is a {}", "mess").log();
    } finally {
      //System.out.println("I cannot believe this");
    }
  }

//  public static void xmain (String[] argv)
//  {
////    EventLogger.reset();
//    LogEvent event= null;
//    try {
//      event= doing(log, "main creates app");
//      final PlatformLogApp app = new PlatformLogApp();
//      done(log, event);
//      event= doing(log, "main calls app.a");
//      Thread t= new Thread("other") {
//        @Override
//        public void run() {
////          EventLogger.reset();
//          LogEvent event= null;
//          try {
//            event= doing(log, "{} calls app.a",Thread.currentThread().getName());
//            app.ma();
//            done(log,event);
//          } catch ( Exception e ) {
//            caught(log,e,event);
//          }
//        }
//      };
//      t.start();
//      app.ma();
//      done(log, event);
//    } catch ( Exception e ) {
//      caught(log,e,event);
//    }
////    EventLogger.reset();
//  }
//
//  private void ma ()
//  {
//    LogEvent event= null;
//    try {
//      event= doing(log,"method {} calls method {}",'A', 'B');
//      mb();
//      done(log,event);
//    } catch ( Exception e ) {
//      throw throwing(log,new RuntimeException("ma cannot complete the operation",e),event);
//    }
//  }
//
//  private void mb ()
//  {
//    mc();
//  }
//
//  private void mc ()
//  {
//    LogEvent event= null;
//    try {
//      event= doing(log,"method {} calls method {}",'C', 'D');
//      md();
//      done(log,event);
//    } catch ( Exception e ) {
//      throw throwing(log,new RuntimeException("mc cannot complete the operation",e),event);
//    }
//  }
//
//  private void md ()
//  {
//    LogEvent event= null;
//    try {
//      event= doing(log, "method {} calls method {}",'D', 'E');
//      me();
//      done(log,event);
//      notice(log, Level.ERROR, "md does not want to proceed");
//      throw new Exception("md has encountered a problem");
//    } catch ( Exception e ) {
//      throw throwing(log,new RuntimeException("md cannot complete the operation",e),"error in md execution");
//    }
//  }
//
//  private void me ()
//  {
//    LogEvent event= null;
//    try {
//      event= doing(log, "method {} does it",'E');
//      notice(log, Level.INFO, "doing it!");
//      done(log,event);
//    } catch ( Exception e ) {
//      throw throwing(log,new RuntimeException("me cannot complete the operation",e),"error in me execution");
//    }
//  }

}
