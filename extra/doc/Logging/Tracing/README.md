# Tracing

Tracing is not a concept of EMW7 platform logging framework but it is a general concept. 
Usually associated with observability.

In this document is not described what tracing is, but it is focused on how to enable 
log tracing that is get `traceId` and `spanId` into the MDC so that EMW7 platform logging framework can 
print them.

# Micrometer

This section briefly cover what [micrometer](https://micrometer.io/) is. The reason is that the tracing support of the 
EMW7 platform logging framework has been designed after micrometer.  
Micrometer is a:
> Vendor-neutral application observability facade  
> Micrometer provides a facade for the most popular observability systems, allowing you to instrument your JVM-based application code   without vendor lock-in. Think SLF4J, but for observability.

Micrometer composes fo two components:
- [Micrometer Observation](https://docs.micrometer.io/micrometer/reference/observation.html).
- [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/).

What we are interested in is `Micrometer Tracing`:
> Micrometer Tracing is a facade over the Brave and OpenTelemetry tracers that gives insight into complex distributed systems at the level of an individual user request. Identify the root cause of issues faster with distributed tracing. Micrometer Tracing is the successor to the Spring Cloud Sleuth project.

Summing up, `Micrometer Tracing` is an API for tracing from which an application can depend on being agnostic of the implementation. The implementation can be injected at runtime.

# Design

How described in the [documentation](../README.md), the EMW7 platform logging framework tracing is designed after relying on retrieving 
tracing information in the MDC. Specifically such a information is expected to be foung in the `traceId` and `spanId` keys.
It is not very clear who put such a keys in the MDC but it is sure that, using Spring Boot, it is possible to configure the project in 
a mean that such information automagically drops into the MDC: [Tracing :: Logging Correlation IDs](https://docs.spring.io/spring-boot/reference/actuator/tracing.html#actuator.micrometer-tracing.logging).  
But what if it is not wanted to use Spring Boot stack? The EMW7 platform logging framework provides some facilities to get basic tracing compatible with `Micrometer Tracing`. That is:
- EMW7 platform logging framework provides a basic implementation of `Micrometer Tracing`.
- It is possible switch, for example from  EMW7 platform logging framework implementation to Spring Boot impletantion, among `Micrometer Tracing` implementations without changing the application code.

## EMW7 platform logging framework `Micrometer Tracing` implementation

How stated before this implementation it is basic and the main limitation is that there is no hiearchy in the span.

The implementation is automatically enabled if no other `Tracer.class` bean are available by autoconfiguring the `logTracingTracer` bean.  
Such bean depends on a bean of type `LogTracingMDCFacade`: EMW7 platform logging framework autoconfigures one if not other beans of such a type are available.  
Because of tracing can be disabled at all (that is EMW7 platform logging framework does not print any tracing information), two autoconfiguration for  `LogTracingMDCFacade` are provided: one for disabled tracing and one for enabled tracing.  

# How to 

In this section is described how to leverage the tracing facility of the EMW7 platform logging framework.

## Spring Boot

To take advantage of Spring Boot tracing, in addition of the dependency on `com.github.emw7::platform-log`, it is enough adding, as dependencies of the application, the following projects:
- org.springframework.boot::spring-boot-starter-actuator.
- org.springframework.boot::spring-boot-starter-aop.
- io.micrometer::micrometer-tracing-bridge-otel.

For more details refer to the [log with Spring Boot tracing](../../../examples/platform-log-spring-tracing/) example. Looking at the example is warmly suggested as:
- In the [`pom.xml`](../../../examples/platform-log-spring-tracing/pom.xml) there are some interesting exclusions.
- In the [`README.md`](../../../examples/platform-log-spring-tracing/README.md) there are some interesting details that lead EMW7 platform loggin framwork tracing facility.

## Internal tracing

As stated above EMW7 platform logging framework provides basic tracing.

To take advantage of internal tracing, it is enough in make the application dependant on `com.github.emw7::platform-log`.

Application can defines its own implementations of `LogTracingMDCFacade` but I thinks nobody will need and will do.

For more details refer to the [log with internal tracing](../../../examples/platform-log-internal-tracing/) example.
