package com.github.emw7.platform.log;

import java.io.Closeable;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

public class LogEvent implements Closeable {

  private final Marker marker;
  private final String pattern;
  private final Object[] params;
  private final String uuid;

  private final Logger log;

  private final Map<String, String> contextArguments;

  LogEvent(@NonNull final Logger log, @NonNull final Marker marker, @NonNull final String pattern,
      @NonNull final Object[] params, @Nullable Map<String, String> contextArguments) {
    this.uuid = UUID.randomUUID().toString();
    this.log = log;
    this.marker = marker;
    this.pattern = pattern;
    this.params = params;
    this.contextArguments = contextArguments;
  }

  @NonNull
  public LogEvent ctxArg (@NonNull final String key, @Nullable final Object val){
    MDC.put(key, (val == null ) ? "null" : val.toString());
    return this;
  }

  @NonNull
  String getUuid() {
    return uuid;
  }

  Logger getLog() {
    return log;
  }

  @NonNull
  String getPattern() {
    return pattern;
  }

  @NonNull
  Object[] getParams() {
    return params;
  }

  @Override
  public void close() {
    if (!CollectionUtils.isEmpty(contextArguments)) {
      contextArguments.keySet().forEach(MDC::remove);
    }
  }
}
