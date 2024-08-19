package com.github.emw7.bar.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.ZonedDateTime;
import java.util.Arrays;

// an event that happens at time t, event is identified by type and each occurrence is identified
// by id; event has a severity and a state either start / end or push.
public record Bar(String id, String type, String descriptionLabel, Severity severity, State state,
                  ZonedDateTime t) {

  public static final String RESOURCE_NAME= Bar.class.getName();

  public static enum Severity {
    NONE(0), LOW(1), MEDIUM(2), HIGH(3), CRITICAL(4);

    // this way deserializes enum using severity property and not internal ordinal().
    @JsonCreator
    public static Severity valueOf (int severity)
    {
      for ( Severity s: values() ) {
        if ( s.severity() == severity ) {
          return s;
        }
      }
      return NONE;
    }

    private final int severity;

    private Severity(int severity) {
      this.severity = severity;
    }

    // this way serializes enum using severity property and not internal name().
    @JsonValue
    public int severity() {
      return severity;
    }

  }

  // this enum does not override serialization and deserialization so can be deserialized either
  //  by name() or ordinal(), and it is serialized by name().
  public static enum State {
    PUSH(-1), START(1), END(0);

    public static State valueOf (int state)
    {
      for ( State s: values() ) {
        if ( s.state() == state ) {
          return s;
        }
      }
      return PUSH;
    }

    private final int state;

    private State(int state) {
      this.state = state;
    }

    public int state () {
      return state;
    }
  }

}
