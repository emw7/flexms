package com.github.emw7.platform.log;

import static com.github.emw7.platform.log.EventLogger.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

// TODO spostare in test misc, other, ...
public class PlatformLogApp {

  private static final Logger log= LoggerFactory.getLogger(PlatformLogApp.class);

  public static void main (String[] argv)
  {
//    EventLogger.reset();
    LogEvent event= null;
    try {
      event= doing(log, "main creates app");
      final PlatformLogApp app = new PlatformLogApp();
      done(log, event);
      event= doing(log, "main calls app.a");
      Thread t= new Thread("other") {
        @Override
        public void run() {
//          EventLogger.reset();
          LogEvent event= null;
          try {
            event= doing(log, "{} calls app.a",Thread.currentThread().getName());
            app.ma();
            done(log,event);
          } catch ( Exception e ) {
            caught(log,e,event);
          }
        }
      };
      t.start();
      app.ma();
      done(log, event);
    } catch ( Exception e ) {
      caught(log,e,event);
    }
//    EventLogger.reset();
  }

  private void ma ()
  {
    LogEvent event= null;
    try {
      event= doing(log,"method {} calls method {}",'A', 'B');
      mb();
      done(log,event);
    } catch ( Exception e ) {
      throw throwing(log,new RuntimeException("ma cannot complete the operation",e),event);
    }
  }

  private void mb ()
  {
    mc();
  }

  private void mc ()
  {
    LogEvent event= null;
    try {
      event= doing(log,"method {} calls method {}",'C', 'D');
      md();
      done(log,event);
    } catch ( Exception e ) {
      throw throwing(log,new RuntimeException("mc cannot complete the operation",e),event);
    }
  }

  private void md ()
  {
    LogEvent event= null;
    try {
      event= doing(log, "method {} calls method {}",'D', 'E');
      me();
      done(log,event);
      notice(log, Level.ERROR, "md does not want to proceed");
      throw new Exception("md has encountered a problem");
    } catch ( Exception e ) {
      throw throwing(log,new RuntimeException("md cannot complete the operation",e),"error in md execution");
    }
  }

  private void me ()
  {
    LogEvent event= null;
    try {
      event= doing(log, "method {} does it",'E');
      notice(log, Level.INFO, "doing it!");
      done(log,event);
    } catch ( Exception e ) {
      throw throwing(log,new RuntimeException("me cannot complete the operation",e),"error in me execution");
    }
  }

}
