package com.github.emw7.platform.telemetry.autoconfig;

import com.github.emw7.platform.telemetry.tracing.Tracing;
import com.github.emw7.platform.telemetry.tracing.TracingFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformTelemetryAutoConfig {

  /**
   * Creates a {@code tracingFactory} bean in case none is yet provided.
   * <p>
   * The created tracing factory creates {@link TracingDefault}.
   *
   * @return a {@code tracingFactory} bean
   */
  @ConditionalOnMissingBean(name = "tracingFactory", value = TracingFactory.class)
  @Bean
  public TracingFactory tracingFactory() {
    return new TracingFactory() {
      @Override
      public @NonNull Tracing of() {
        return new TracingDefault();
      }

      @Override
      public @NonNull Tracing of(@NonNull final String traceId) {
        return new TracingDefault(traceId);
      }

    };
  }

}
