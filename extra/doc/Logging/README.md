# Logging

The platform-log platform project provides the EMW7 platform logging framework.  
The EMW7 platform logging framework is based on the concept of log event.  
Classic logging systems allow to log messages by choosing the type of message:
error, warning, informational, debug and tracing messages.  
The instruction `logger.error("this is an error")` prints an error message,
while the instruction `logger.info("this is information")` prints an informational message.  
The message types are placed in a hierarchical scale, and it is possible to enable only the types from a 
certain level onwards. Usually the hierarchy (starting from the lowest level) is: `trace`, `debug`, 
`info`, `warn`, `error`.  
By enabling the info level, only info, warning and error messages are printed and so the 
instruction `logger.debug("this is for debugging")` does not produce any effect, that is, the 
message is not be printed.  

The EMW7 platform logging system (from now on event logger) changes the message types by defining 
the following types: `notice`, `doing`, `done`, `throwing` and `caught`.  
These types are called log evenys.  
Each of these types is associated with a classic type (which from now on will be called level) which,
however, can be changed, with some constraints, when you write the logging instruction.  
Example (we will see the details of these instructions in a bit, which are, however, partial):
`EventLogger.notice(log,...)` prints the notice message with its default level, instead, 
`EventLogger.notice(log,...).warn()` prints the notice message at warning level.  
An important thing to note is that event logger delegates the actual printing of the message to SLF4J
which in turn is just a facade for the actual logging system chosen (see [Architectiure and desing](#architecture-and-design)).
So, assuming that this system is based on levels, it is possible to decide the minimum level to 
log at, so that by enabling the warning level, the first message would not be printed 
(since the default level for the notice event is informational), while the second message would be 
printed.

## Log keys

In addition to switching from log levels to log events, event logger strongly formalizes the 
structure of log messages.  
This structure can be changed and relaxed by some [configuration parameters](#configuration-parameters).  
The log message is composed of various tokens.  
A token can have two forms:
- [#key:value]
- [#key:name=value]

The first form is assumed by the following keys: `event`, `uuid`, `ex-msg`, `ex-type`, 
`ex-cause-msg`, `ex-cause-type`, `result`.  
The second form is assumed by the following keys: `arg`.  
Not all keys are applicable to all events. 
Furthermore, not all keys are added by default by the various events. 
For the details, refer to the documentation of the various events.  
An example of a log message: `[#event:caught][#uuid:9c3ccda1-1196-4269-89da-ab1b1519a85a][#what:caught exception][#ex-msg:an exception][#ex-type:java.lang.Exception][#ex-cause-msg:null][#ex-cause-type:java.lang.NullPointerException][#arg:iid=123 ][#arg:tid:xyz]`

### Event key

The event key is the name of the event and is defined by the event itself and therefore cannot be 
changed in the log statement.

### What key

The what key is composed of a pattern and a list of parameters.  
The pattern is a string that can contain placeholders (in the form `{}`) that are replaced 
positionally by the parameters.  
Example:  
> pattern: Today is {} and therefore I wear {}  
> params: [hot, t-shirt]  
> => Today is hot and I wear a t-shirt.  

Pattern and params must be defined in the log statement.

### Arg key

An arg is a pair (name, value) that characterizes the log message.
For example, in a loop a log statement could always use the same pattern (`looking for entity`)
and change the value of the arg at each iteration (for example` ("id",#)`, with # iteration number).
Args differ from pattern params because the latter ends up inside the message in a way that may not 
be structured, making log analysis more difficult. The former, on the other hand, is printed in 
a formal way.
With the arg key, both log message-specific and context args are printed.  
For context args, see the [Log context](#log-context) section.  
Log message-specific args must be defined in the log statement
The arg key can be repeated multiple times, and for non-context args,
it must be defined in the log statement.

## Log context

The `EventLogger#context` method returns a `LogContext` instance that allows you to manage context args.
Context args are args that have a life cycle that extends across multiple log events.  
A context arg is printed by all events that come after its definition and before its removal.
Since an exception could cause the context args to be lost,
the `LogContext` class is `AutoClosable`
and can be used in a `try-with-resources` statement so that all added context args are removed.  
Of course,
not using a `try-with-resources` statement allows you to explicitly invoke the #close() method,
but not using either method means (due to the way context args are implemented[*])
leaving those context args available to log events that shouldn't know about them.  

### Usage in a try-with-resources statement

If it is needed that context args are printed in a catch block then the following code does not work:

```java
try (LogContext context = context(Arg.arg("section", 123))) {
    example(...); // throws an exception.
    // the log contains args from context (section=123).
    notice(log, "starting...") // logs [#event:notice][#what:starting...][#arg:section=123]
} catch (Exception e ) {
  caucht(log, e, ...); // does not print the context argument section
}
```
It does not work because the catch block is out of the try block and so the {@code context} has been already closed.  
If it is needed that context args are printed in a catch block then the exception must be caught in the try block and that can be reached by adding a wrapping method:
```java
try (LogContext context = context(Arg.arg("section", 123))) {
    callExampleAndCatch(...); // calls the wrapping method.
    // the log contains args from context (section=123).
    notice(log, "starting...") // logs [#event:notice][#what:starting...][#arg:section=123]
}

// the wrapping method.
void callExampleAndCatch (...) {
  try {
    example(...); // throws an exception.
  } catch ( Exception e ) {
    caucht(log, e, ...); // prints the context argument section
  }
}
```

[*] Context args are handled via SLF4J's Mapped Diagnostic Context
(https://www.slf4j.org/manual.html#mdc)

## Log events

Before going into the details of event logger and therefore its architecture and design,
it is useful to define the different events.
- doing:
  it is used to notify that something important is ongoing:
  ("delete all users") and must be coupled with the done event
- done: it closes the doingevent, it may or may not have a result.
- notice:
  it is an extemporaneous event,
  for example, to notify that "the entity you were looking for has been found"
  and is an alternative to the doing..done construct when this would be excessive.
- caught: it is used to notify that an exception has been caught.
- throwing: it is used to notify that an exception is about to be thrown; it can also be used for 
  checked exceptions, but it is more useful to use it with unchecked exceptions since the former 
  must be caught so there should be a caught somewhere, while for the latter there may not be the 
  caught, so it is good to know when it is thrown.

The entry point of the event logger is the `EventLogger` class that is an utility class that exposes a set of methods named after the events described above.  
Each of those methods returns an event builder that in addition to the methods that allow to set the characteristics of the event, expose the `log()` method that has to responsability:
- Creating the log event.
- Invoking the `log()` method (that actually prints the log message) on the log event.

The reason for which it is the builder that invokes such a method it is to get a more compact syntax:  
> `EventLogger.&lt;event&gt;.log()` instead of  
> `EventLogger.&lt;event&gt;.build().log()`

The following is the sequence diagram of the flow depicted above:
![WZV Sequence diagram](./W9N%20Logging%20-%20WZV%20Sequence%20diagram.png)
*WZV Sequence diagram*

## Configuration parameters

- **always-print-uuid**: configured by `com.github.emw7.platform.log.always-print-uuid` system
  property or by related environment variable; default: `false`; if set to `true` then `uuid` log
  key is printed for all log events and not only for the ones that enable it; if set to `false`
  then `uuid` log key is printed only if log event enable it; refer to specific log event
  documentation to known whether it enables or not the printing of the `uuid` log key.
- **log-on-thread**: configured by `com.github.emw7.platform.log.log-on-thread` system property or
  by related environment variable; default: `false`; if set to `true` then log event delegates
  actual printing of log message to a thread; if set to `false` then log event calls SLF4J directly.
  See [log-on-thread](#log-on-thread) for more information.
- **shutdown-timeout**: configured by `com.github.emw7.platform.log.shutdown-timeout` system
  property or by related environment variable; default: `60`; seconds the system wait for logging
  task to complete before forcing shutdown and waiting again `shutdown-timeout` seconds; used only
  if `log-on-thread` is `true`.

### log-on-thread

**ATTENTION**: even if this feature has been designed to improve the performance, it has not been
benchmarked so there is not proof that enabling it actually improves the performance.

Log on thread is a feature that, if enabled, actual invocation of SLF4J is delegated, by log 
event, to a thread. This feature has been designed to improve the performance as actual printing 
of a log message is time-consuming and so delegating it to a thread allows the application to go 
ahead while the log message is printed. Printing to a thread arises three issues to be addressed: 
1. Inheritance of context arguments. 
2. Flushing the thread queue when the application terminates.
    1. Termination of the actual logging subsystem.

To address point 1. it is needed to submit to the thread a runnable that before doing any action set
the context argument map to be equals to the one of the invoking thread. That can be expensive and
could be one of the points that prevents getting performance improvement.

Point 2. is quite easy to be satisfied: using an executor and adding a shutdown hook that 
requests a clean shutdown of the executor in order to refuse new task but allowing for the already 
submitted ones to complete.

In fact, point 2.1 seems to be necessary only with LOG4J2, which registers a shutdown hook that,
apparently, prevents the log message queue from being emptied because it disables LOG4J2. However,
since not all logging systems have been tested, it’s possible that the same issue exists with other
systems. Therefore, if not all log messages are being printed, the cause could be the same as that
described for LOG4J2, and a specific solution outside the scope of platform-log needs to be found.
This solution might be similar to those proposed below for LOG4J2.

For LOG4J2, there are two possible solutions. The first is to use the `shutdownHook` attribute with
the value `disable` in the `configuration` tag: `<configuration status="WARN" monitorInterval="30"
shutdownHook="disable" ...>`. This disables the LOG4J2 shutdown hook, which must be shut down
programmatically. The application will then need to register a shutdown hook. A possible solution
that uses the countdown latch `terminated`, which is reset by the shutdown hook of `LogEvent` when it
has finished flushing the queued log events:
```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    try {
    LogEvent.terminated.await();
    LogManager.shutdown();
  } catch (InterruptedException e) {
    LogManager.shutdown();
    Thread.currentThread().interrupt();
  }
}));
```

The second solution is to use the `shutdownTimeout` attribute, whose value specifies the milliseconds
to wait before executing the actions of the shutdown hook in the `configuration` tag: `<configuration
status="WARN" monitorInterval="30" shutdownTimeout="<milliseconds>" ...>`. However, this solution has
not been successfully implemented (it seems that the attribute is completely ignored) and is based
on a heuristic, so the first solution is preferred.

## Setups for different logging subsystems

Here is briefly described how to bind some SLF4J providers.  
More information is available at [Linking with a logging framework at deployment time
](https://www.slf4j.org/manual.html#swapping).

### reload4j

Description of [reload4j](https://reload4j.qos.ch/).

Example available in [extra/examples/reload4j](../../../platform-log/extra/examples/reload4j).

### log4j2

Example available in [extra/examples/log4j2](../../../platform-log/extra/examples/log4j2).

### logback

Description of [logback](https://logback.qos.ch/).

Example available in [extra/examples/logback](../../../platform-log/extra/examples/logback).

### log4j

No configuration and no example are provided as SL4J redirects to [reload4j](#reload4j) ([excerpt from SLF4J documentation, slf4j-log4j12-2.0.16.jar section](https://www.slf4j.org/manual.html#swapping)):
> Binding/provider for log4j version 1.2, a widely used logging framework. Given that log4j 1.x has been declared EOL in 2015 and again in 2022, as of SLF4J 1.7.35, the slf4j-log4j module automatically redirects to the slf4j-reload4j module at build time. Assuming you wish to continue to use the log4j 1.x framework, we strongly encourage you to use slf4j-reload4j instead.

## Architecture and design

The architecture and design of event logger is very simple.  
In practice, it is a wrapper for classic log systems.  
In fact each event is created starting from a SLF4J logger and then delegates to it the printing 
of the log.  
Example:
> in `notice(log, ...)` statement the `log` argument must be something like this:  
> `org.slf4j.LoggerFactoryLoggerFactory.getLogger(Example.class);`  
> and then notice requires printing the log in the following way:  
> `log.atLevel(level).addMarker(marker).....log()`

So with reference to the message shown above a possible complete log message (using log4j2 and the 
following log4j2 log pattern defined, for example, in `log4j2.xml` configuration file: `%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n`) would be:  
`2024-10-04 14:11:32 ERROR Example - [#event:caught][#uuid:9c3ccda1-1196-4269-89da-ab1b1519a85a][#what:caught exception][#ex-msg:an exception][#ex-type:java.lang.Exception][#ex-cause-msg:null][#ex-cause-type:java.lang.NullPointerException][#arg:iid=123][#arg:tid:xyz]`

The following diagram depicts the hierarchies and competence:
![WY8 Hierarchy and competence diagram](./W9N%20Logging%20-%20WY8%20Hierarchy%20and%20competence%20diagram.png)
*WY8 Hierarchy and competence diagram*
