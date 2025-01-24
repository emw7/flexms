# Platform :: Examples :: platform-log-spring-tracing

This example shows how to exploit platform-log Spring Boot tracing.

In this document, there are some references to observability in addition to tracing
because they are somewhat related in general and in Spring Boot too.

This document can be, in some parts, not so straightforward.

This project shows platform-log working together with Spring Boot tracing when the application uses [OpenTelemetry](https://opentelemetry.io/).

The project started by merging 
[Distributed Request Tracing — Spring Boot 3, Micrometer Tracing with OpenTelemetry](https://github.com/yashodhah/spring-boot-observability) 
and information mainly from:
- [Observability with Spring Boot 3](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3).
- [Distributed Request Tracing — Spring Boot 3, Micrometer Tracing with OpenTelemetry](https://medium.com/javarevisited/distributed-request-tracing-spring-boot-3-micrometer-tracing-with-opentelemetry-3fb129ec8753) 
  with a lot of useful references and related 
  [source code](https://github.com/yashodhah/spring-boot-observability).
- [OpenTelemetry Setup in Spring Boot Application](https://www.baeldung.com/spring-boot-opentelemetry-setup).

For other useful links see [References](#references) below.

# Tracing (and Observability) understanding

This section describes my understanding of Micrometer, OpenTelemetry, tracing and observability (with Spring Boot), and related concepts. The notes are scattered and may contain errors.

Previously, there was `Spring Cloud Sleuth`, which has been replaced by **Micrometer** and **Micrometer Tracing** (from here on, referred as Micrometer).  
`Spring Boot 3` (with `Spring 6`) introduces **Spring Observability**, which is built on top of Micrometer.

Micrometer is an API that performs minimal functionality by itself and relies on a provider for actual operations. Currently (as of November 2024), the two providers are:
- **OpenTelemetry**
- **OpenZipkin’s Brave**

Providers implement the APIs defined by Micrometer and enable the collection of [metrics](https://docs.micrometer.io/micrometer/reference/concepts.html) in an application or distributed system.  
Note that collecting metrics can be misleading: metrics collected via Micrometer APIs are lost unless sent to a system capable of storing them. It is not Micrometer’s responsibility to define how metrics are transferred to collection systems. Each provider integrates with such systems differently.

**OpenTelemetry** defines a transfer protocol (OTLP). It is agnostic to the backend system, as long as that system implements the OpenTelemetry protocol. For instance, [Jaeger](https://www.jaegertracing.io/) and [Prometheus](https://prometheus.io/) support OTLP.  
The integration approach for **Brave** has not been explored in this document.

The project described in the post [Observability with Spring Boot 3](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3) is configured to send:
- Metrics to Prometheus
- Completed spans to [Zipkin](https://zipkin.io/)
- Logs to [Grafana Loki](https://grafana.com/oss/loki/)

These systems seem specialized for subsets of metrics rather than being general-purpose. Alternatively, the choice to use multiple systems may demonstrate the flexibility of Spring Observability.

Micrometer introduces the concept of `instrumentation`. Third-party projects (e.g., libraries) can emit metrics by [instrumenting themselves](https://docs.micrometer.io/micrometer/reference/observation/instrumenting.html).  
There is also a list of [pre-instrumented projects](https://docs.micrometer.io/micrometer/reference/observation/projects.html).

# The project

Lo scopo del progetto è quello di far vedere come stampare `trace-id` e `span-id` nelle righe di 
log usando [EMW7 platform logging framework](../../doc/Logging/README.md).

The project is configured to used `logback` that, in turn, is configured with a pattern that prints `traceId` 
and `spanId` MDC fields. Such a fields are put in the MDC by micrometer as ([Observability with Spring Boot 3 :: WebMvc Server Setup](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3#webmvc-server-setup)):
> Since we have Micrometer Tracing on the classpath, the logs are automatically correlated (that is, they contain a unique trace identifier).

The following are the minimal dependencies set that has been identified to enable tracing:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>
```

Following is the output of an execution of the example with this configuration:
- com.github.emw7.platform.log.trace-data-not-available-label=N/A.

The rest of the section will refer to this output as a reference to explain some concepts.
```shell
       _                              _     _ _ _ _                _
      | |                            | |   (_) (_) |              | |
  ___ | |__  ___  ___ _ ____   ____ _| |__  _| |_| |_ _   _ ______| | ___   __ _
 / _ \| '_ \/ __|/ _ \ '__\ \ / / _` | '_ \| | | | __| | | |______| |/ _ \ / _` |
| (_) | |_) \__ \  __/ |   \ V / (_| | |_) | | | | |_| |_| |      | | (_) | (_| |
 \___/|_.__/|___/\___|_|    \_/ \__,_|_.__/|_|_|_|\__|\__, |      |_|\___/ \__, |
                                                       __/ |                __/ |
                                                      |___/                |___/

Version 
Spring boot 3.3.5

2024-11-25 15:17:04.833 [main] INFO  c.g.e.e.o.ExampleApplication -- Starting ExampleApplication using Java 21.0.3 with PID 33196 (.../target/classes started by ... in .../extra/examples)
2024-11-25 15:17:04.836 [main] INFO  c.g.e.e.o.ExampleApplication -- No active profile set, falling back to 1 default profile: "default"
2024-11-25 15:17:06.134 [main] INFO  c.g.e.e.o.ExampleApplication -- Started ExampleApplication in 1.692 seconds (process running for 2.136)
2024-11-25 15:17:06.189 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:Hello, world, from actionA: main!]
2024-11-25 15:17:06.189 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:This is subActionAA: actionA:1!]
2024-11-25 15:17:06.190 [main] INFO  c.g.e.e.o.logic.ExampleServiceA -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:0d147832cdf99a9e][#what:this is ExampleServiceA#serviceAActionSAA called from actionA]
2024-11-25 15:17:06.193 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:9a1d525adbca43a1][#what:T1 before]
2024-11-25 15:17:06.194 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:9a1d525adbca43a1][#what:T1 after]
2024-11-25 15:17:06.194 [T1] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:This is actionT: t1!]
2024-11-25 15:17:06.195 [T2] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:This is actionT: t2!]
2024-11-25 15:17:06.195 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:bfbb204728ae6c37][#what:Hello, world, from actionB: actionA!]
2024-11-25 15:17:06.195 [T3] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:613d80e52f562b50e31f1f1884220081][#spanId:b8a215c862f70610][#what:This is actionT: t3!]
2024-11-25 15:17:06.195 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:This is subActionAA: actionA:2!]
2024-11-25 15:17:06.195 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:ef198c000a3d31681a900d189184f3b5][#spanId:1634ddeb1b571c32][#what:Hello, world, from actionC: actionA!]
2024-11-25 15:17:06.196 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:N/A][#spanId:N/A][#what:Hello, world, from actionB: main!]
2024-11-25 15:17:06.197 [main] INFO  c.g.e.e.o.api.ExampleController -- [#event:notice][#traceId:4240eac59ceb21df281d7715b1754819][#spanId:ee0c1f87609f4aee][#what:Hello, world, from actionC: main!]
```

The project is very simple. The `SpringBootApplication` invokes the `actionA`, `actionB` and `actionC` 
methods of the controller.  
Comparing the fourth and the one before last log's row above can be noted that the one of `actionA` contains 
the trace information (traceId and spanId) while the one of `actionB` does not contain it.  
The magic of `actionA` stays on the `@Observed` annotation. Using such an annotation, trigger 
micrometer to wrap the methods in a trace context, that is with a `traceId` and a `spanId`. 
The first three rows do not contain the trace information because the tracing in the log is managed 
by `EventLogger` directly and not by logback. If the tracing were managed by logback (with this 
pattern, for example: `<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} -%kvp- [tid:%X{traceId:-N/A}][sid:%X{spanId:-N/A}] %msg%n</pattern>`) then 
the first three rows would contain: `[tid:N/A][sid:N/A]`.
The `actionC` (that is `@Observed`):
- Reuses the tracing context when called from within `actionA` (because called internally by a 
  spring public method, that is, its invocation does not pass through the spring proxy):
  4240eac59ceb21df281d7715b1754819/ee0c1f87609f4aee.
- Start a new tracing context when called from the `SpringBootApplication`: ef198c000a3d31681a900d189184f3b5/1634ddeb1b571c32.

Another interesting thing to be stressed is that then of `ExampleServiceA#serviceAActionSAA` is 
called from within `ExampleController#actionA` a new span-id is started but the trace-id is 
preserved: ef198c000a3d31681a900d189184f3b5/0d147832cdf99a9e.

It is possible to [manage trace information directly](https://docs.micrometer.io/tracing/reference/api.html).  
That is done for the call of `ExampleController#actionB` from within `ExampleController#actionA` by 
creating a new span and wrapping the call with a `Tracer.SpanInScope` in a `try-with-resource`. In 
the `finally` block the new span is `ended`:
```java
final Span aaab = tracer.nextSpan().name("ab-from-aa");
try (Tracer.SpanInScope ws = tracer.withSpan(aaab.start())) {
  actionB("actionA");
} finally {
  aaab.end();
}
```
The code above maintains the trace-id, while changes the span-id: ef198c000a3d31681a900d189184f3b5/bfbb204728ae6c37 as 
it happens for `ExampleServiceA#serviceAActionSAA` call described above.

The things are less straightforward in case of threads (1). The example spawns three threads.  
The first (T1) in a `Tracer.SpanInScope` but for some reason trace information are the same 
of the thread spawner: ef198c000a3d31681a900d189184f3b5/1634ddeb1b571c32 even if the `before` and 
`after` logs are in a different span.  
The second (T2) is a plain thread and uses the trace information of the spawner (but that is expected).  
The third change runs the code in a `Tracer.SpanInScope` and in this case both trace-id and span-id change. I 
do not think this is the desired behaviour. So it must be investigated

_(1)_ : In each thread is it executed the code `MDC.setContextMap(contextMap);`; firstly I thought that 
was needed because of logback, but the same is needed with log4j2; without that instruction T1 and T2 
has not trace information.

The 
[application configuration](src/main/java/com/github/emw7/examples/logtracing/config/ExampleAppConfig.java) 
contains the following commented out code:
```java
//  // To have the @Observed support we need to register this aspect
//  @Bean
//  ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
//    return new ObservedAspect(observationRegistry);
//  }
```

It has been kept from [[Observability with Spring Boot 3 :: WebMvc Server Code](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3#webmvc-server-code), 
but it seems to be not needed. Likely it was needed in previous versions and now a default aspect 
it is already instantiated by the Spring Observability stack.

# References

Not in a particular order:
- https://www.w3.org/TR/trace-context/.
- https://docs.micrometer.io/.
- https://www.baeldung.com/java-brave.
- [Tracing with Spring Boot, OpenTelemetry, and Jaeger](https://reflectoring.io/spring-boot-tracing/).
- [Observability With Spring Boot](https://www.baeldung.com/spring-boot-3-observability)
