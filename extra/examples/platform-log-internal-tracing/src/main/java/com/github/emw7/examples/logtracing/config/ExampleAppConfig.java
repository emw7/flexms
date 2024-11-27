package com.github.emw7.examples.logtracing.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleAppConfig {

  // comment out this code to use the default MDCWrapper provided by platform-observability that
  // does nothing, and so there is no tracing at all.
//  @Bean
//  public MDCWrapper mdcWrapper ()
//  {
//    return new MDCWrapper() {
//      @Override
//      public void traceId(@NonNull final String s) {
//        MDC.put("traceId", s);
//      }
//
//      @Override
//      public void spanId(@NonNull final String s) {
//        MDC.put("spanId", s);
//      }
//
//      @Override
//      public boolean containsTraceId() {
//        return !StringUtils.isEmpty(MDC.get("traceId"));
//      }
//
//      @Override
//      public boolean containsSpanId() {
//        return !StringUtils.isEmpty(MDC.get("spanId"));
//      }
//    };
//  }

}
