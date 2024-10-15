# Logging

The platform-log platform project provides the EMW7 platform logging framework.  
The EMW7 platform logging framework is based on the concept of log event.  
Classic logging systems allow you to log messages by choosing the type of message:
error, warning, informational, debug and tracing messages.  
The instruction `logger.error("this is an error")` will print an error message,
while the instruction logger.info("this is information") will print an informational message.  
The message types are placed in a hierarchical scale, and you can enable only the types from a 
certain level onwards. Usually the hierarchy (starting from the lowest level) is: `trace`, `debug`, 
`info`, `warn`, `error`.  
By enabling the info level, only info, warning and error messages will be printed and so the 
instruction `logger.debug("this is for debugging")` will not produce any effect, that is, the 
message will not be printed.  

The EMW7 platform logging system (from now on event logger) changes the message types by defining 
the following types: `notice`, `doing`, `done`, `throwing` and `caught`.  
These types are called log events.  
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
For details, refer to the documentation of the various events.  
An example of a log message: `[#event:caught][#uuid:9c3ccda1-1196-4269-89da-ab1b1519a85a][#what:caught exception][#ex-msg:an exception][#ex-type:java.lang.Exception][#ex-cause-msg:null][#ex-cause-type:java.lang.NullPointerException][#arg:iid=123 ][#arg:tid:xyz]`

### Event key

The event key is the name of the event and is defined by the event itself and therefore cannot be 
changed in the log statement.

### What key

The what key is composed of a pattern and a list of params.  
The pattern is a string that can contain placeholders (in the form `{}`) that are replaced 
positionally by the params.  
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
  it is used to notify that something important is being done
  ("deleting all users") and must be coupled with the done event:
  it closes the doing, it may or may not have a result.
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

Log on thread is a feature that if enabled, then actual invocation of SLF4J is delegated, by log 
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

In realtà il punto 2.1 sembra essere necessario solo con LOG4J2 che registra uno shutdown hook 
che, a quanto pare, impedisce di svuotare la coda di messaggi di log perché disabilita LOG4J2.  
Comunque, siccome non stati provati tutti i sistemi di log potrebbe essere che lo stesso problema 
ci sia con altri sistemi per cui se non tutti i messaggi di log vengono stampati, la causa 
potrebbe essere la stessa di quella descritta per LOG4J2 e per cui va trovata una soluzione 
specifica che esula dallo scopo di platform-log. Soluzione che potrebbe essere simile a quelle 
proposte di seguito per LOG4J2.
Per LOG4j2 ci sono 2 possibili soluzioni. 
La prima è utilizzare l'attributo `shutdownHook` con valore `disable` nel tag `configuration`: 
`<Configuration status="WARN" monitorInterval="30" shutdownHook="disable" ...>`. 
Questo disabilita lo shutdown hook di LOG4J2 che però va spento in modo programmatico. L'applicazione 
dovrà quindi registrare uno shutdown hook. Una possibile soluzione che sfrutta il count down latch 
`terminated` azzerato dallo shutdown hook di `LogEvent` quando ha finito di fare il flushing dei log event 
accodati:
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
La seconda è utilizzare l'attributo `shutdownTimeout` il cui valore specifica i millisecondi da attendere prima di eseguire le azioni dello shutdown hook
nel tag `configuration`:
`<Configuration status="WARN" monitorInterval="30" shutdownTimeout="<milliseconds>" ...>`.
Questa soluzione però non si è riusciti a farla funzionare (sembra che l'attributo sia del tutto 
ignorato) e comunque si basa su una euristica per cui è preferibile la prima.
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

These are the maven dependencies with scope `runtime` but they could be set as `provided`:
```xml
<dependencies>
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-reload4j</artifactId>
    <version>${slf4j-reload4j.version}</version>
    <scope>runtime</scope>
  </dependency>
  <!-- <dependency>
    <groupId>ch.qos.reload4j</groupId>
    <artifactId>reload4j</artifactId>
    <version>1.2.25</version>
    <scope>runtime</scope>
  </dependency> -->
</dependencies>
```

The `ch.qos.reload4j.ch.qos.reload4j` is commented out as `org.slf4j.slf4j-reload4j` depends on 
it and it is not needed to be declared, but in case they will be `provided` then the jar defined 
by `ch.qos.reload4j.ch.qos.reload4j` must be provided.

And, because reload4j is a replacement for LOG4J 1.x then a `log4j.properties` must be provided. 
Here a basic example:
```properties
# For the general syntax of property based configuration files see
# the documentation of org.apache.log4j.PropertyConfigurator.

# The root category uses two appenders: default.out and default.file.
# The first one gathers all log output, the latter only starting with
# the priority INFO.
# The root priority is DEBUG, so that all classes can be logged unless
# defined otherwise in more specific properties.
log4j.rootLogger=DEBUG, default.out

# System.out.println appender for all classes
log4j.appender.default.out=org.apache.log4j.ConsoleAppender
log4j.appender.default.out.threshold=DEBUG
log4j.appender.default.out.layout=org.apache.log4j.PatternLayout
log4j.appender.default.out.layout.ConversionPattern=%-5p %c: %m%n
```

### log4j2

These ar ethe maven dependencies (scope considerations are the same-made for [reload4j](#reload4j): 
```xml
<dependencies>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j2-impl</artifactId>
    <version>${log4j-slf4j2-impl.version}</version>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>${log4j2.version}</version>
  </dependency>
</dependencies>
```

Note that the dependency against `org.apache.logging.log4j.log4j-core` it is not `runtime` scoped 
because in the demo application it has been implemented the shutdown hook that depends on 
`LogManager` that is in `org.apache.logging.log4j.log4j-core`. In case no shutdown hook was not
 implemented, then such a dependency could be `runtime` scoped. As for [reload4j](#reload4j) the 
`runtime` scope can be `provided` if dependencies are provided.

And the following there is a basic configuration example to be put in `log4j2.xml` for example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" monitorInterval="30" shutdownHook="disable">
  <Properties>
    <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} CTX-ARGS:%X - %m%n</Property>
  </Properties>

  <Appenders>
    <Console name="console" target="SYSTEM_OUT" follow="true">
      <PatternLayout pattern="${LOG_PATTERN}"/>
    </Console>
  </Appenders>

  <Loggers>
    <Root level="debug">
      <AppenderRef ref="console"/>
    </Root>
  </Loggers>
</Configuration>
```

Note the `shutdownHook` tag in the configuration above.

### logback

Description of [logback](https://logback.qos.ch/).

These are the maven dependencies with scope `runtime` but they could be set as `provided` (see 
[reload4j](#reload4j)):
```xml
<dependencies>
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>${logback-classic.version}</version>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

And the following there is a basic configuration example to be put in `logback.xml` for example:
```xml
<configuration>

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <!-- encoders are assigned the type
         ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %X -%kvp- %msg%n</pattern>
    </encoder>
  </appender>

  <root level="debug">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

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
