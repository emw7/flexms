package com.github.emw7.examples.api;

import static com.github.emw7.platform.log.EventLogger.notice;

import com.github.emw7.examples.logic.ExampleServiceA;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class ExampleController {

  private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

  private final ExampleServiceA serviceA;
  private final Tracer tracer;

  public ExampleController(@NonNull final ExampleServiceA serviceA, @NonNull final Tracer tracer) {
    this.serviceA= serviceA;
    this.tracer = tracer;
  }

  @Observed(name = "ExampleController#actionA")
  public String actionA(@NonNull final String src) {

    //log.atLevel(Level.INFO).log("Hello, {}, from {}: {}!", "world", "actionA", src);

    notice(log, "Hello, {}, from {}: {}!", "world", "actionA", src).log();

    subActionAA("actionA:1");

    serviceA.serviceAActionSAA("actionA");

    // I think get here and then set of MDC it is due to logback.
    final Map<String, String> contextMap = MDC.getCopyOfContextMap();

    // T1
    final Span ns = tracer.nextSpan().name("t1");
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(ns.start())) {
      notice(log, "T1 before").log();
      Thread.ofPlatform().name("T1").factory().newThread(() -> {
        MDC.setContextMap(contextMap);
        actionT("t1");
      }).start();
      notice(log, "T1 after").log();
    } finally {
      ns.end();
    }

    // T2
    Thread.ofPlatform().name("T2").factory().newThread(() -> {
      MDC.setContextMap(contextMap);
      actionT("t2");
    }).start();

    // T3
    Thread.ofPlatform().name("T3").factory().newThread(() -> {
      MDC.setContextMap(contextMap);
      final Span newSpan = tracer.nextSpan().name("t3");
      try (Tracer.SpanInScope ws = tracer.withSpan(newSpan.start())) {
        actionT("t3");
      } finally {
        newSpan.end();
      }
    }).start();

    final Span aaab = tracer.nextSpan().name("ab-from-aa");
    try (Tracer.SpanInScope ws = tracer.withSpan(aaab.start())) {
      actionB("actionA");
    } finally {
      aaab.end();
    }

    subActionAA("actionA:2");

    actionC("actionA");

    return "Hello, world!";
  }

  private void subActionAA(@NonNull final String src) {

    //log.atLevel(Level.INFO).log("This is {}: {}!", "subActionAA", src);

    notice(log, "This is {}: {}!", "subActionAA", src).log();
  }

  private void actionT(@NonNull final String src) {

    //log.atLevel(Level.INFO).log("This is {}: {}!", "actionT", src);

    notice(log, "This is {}: {}!", "actionT", src).log();

    //notice(log, "Is tracer the same in tracer container? It is {}", tracer == TracerContainer.getTracer()).warn().log();
  }

  public String actionB(@NonNull final String src) {

    ////log.atLevel(Level.INFO).log("Hello, {}, from {}: {}!", "world", "actionB", arc);

    notice(log, "Hello, {}, from {}: {}!", "world", "actionB", src).log();

    return "Hello, world!";
  }

  @Observed(name = "ExampleController#actionC")
  public String actionC(@NonNull final String src) {
    notice(log, "Hello, {}, from {}: {}!", "world", "actionC", src).log();

    return src;
  }

}
