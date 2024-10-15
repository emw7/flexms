package com.github.emw7.platform.log;

import com.github.emw7.platform.core.mapper.BooleanMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The log event for the doing log events, stressing the starting of an operation.
 *
 * @see EventLogger#doing(Logger, String, Object...)
 */
public class DoingLogEvent extends LogEvent {

  DoingLogEvent(@NonNull final Logger log, @NonNull final Marker marker, @NonNull final String pattern,
      @NonNull final Object[] params, @NonNull final Level level, @NonNull final String event,
      @NonNull final Set<Arg<?>> args, @NonNull final String uuid, final boolean uuidOn) {
    super(log, marker, pattern, params, level, event, args, uuid, uuidOn);
  }
  //endregion Constructors
}
