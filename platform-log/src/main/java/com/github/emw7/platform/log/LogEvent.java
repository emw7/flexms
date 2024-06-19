package com.github.emw7.platform.log;

import java.util.Stack;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.lang.NonNull;

public class LogEvent /*implements AutoCloseable*/{

  private final Marker marker;
  private final String pattern;
  private final Object[] params;
  private final String uuid;

  private final Logger log;
//  private boolean consumed= false;
//
//  @Override
//  public void close() throws Exception {
//    if ( !consumed ) {
//      EventLogger.done(log, this);
//    }
//  }

  public LogEvent(@NonNull final Logger log, @NonNull final Marker marker, @NonNull final String pattern, @NonNull final Object[] params) {
    this.uuid= UUID.randomUUID().toString();
    this.log = log;
    this.marker = marker;
    this.pattern = pattern;
    this.params = params;
  }

  public @NonNull String getUuid() {
    return uuid;
  }

  public Logger getLog() {
    return log;
  }

  public @NonNull String getPattern() {
    return pattern;
  }

  public @NonNull Object[] getParams() {
    return params;
  }


}
