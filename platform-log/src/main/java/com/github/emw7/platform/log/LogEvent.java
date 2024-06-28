package com.github.emw7.platform.log;

import java.util.Stack;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.lang.NonNull;

public class LogEvent {

  private final Marker marker;
  private final String pattern;
  private final Object[] params;
  private final String uuid;

  private final Logger log;

  LogEvent(@NonNull final Logger log, @NonNull final Marker marker, @NonNull final String pattern, @NonNull final Object[] params) {
    this.uuid= UUID.randomUUID().toString();
    this.log = log;
    this.marker = marker;
    this.pattern = pattern;
    this.params = params;
  }

  @NonNull String getUuid() {
    return uuid;
  }

  Logger getLog() {
    return log;
  }

  @NonNull String getPattern() {
    return pattern;
  }

  @NonNull Object[] getParams() {
    return params;
  }

}
