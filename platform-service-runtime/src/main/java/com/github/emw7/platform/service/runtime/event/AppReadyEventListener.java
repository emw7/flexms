package com.github.emw7.platform.service.runtime.event;

import static com.github.emw7.platform.log.EventLogger.notice;

import com.github.emw7.platform.core.CoreConstants;
import com.github.emw7.platform.service.core.runtime.config.AppConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;

/**
 * The autoconfigured event that logs some information from {@link AppConfigProperties} when
 * application is ready.
 */
public final class AppReadyEventListener {

  private static final Logger log = LoggerFactory.getLogger(AppReadyEventListener.class);

  private final AppConfigProperties appConfigProperties;

  public AppReadyEventListener(@NonNull final AppConfigProperties appConfigProperties) {
    this.appConfigProperties = appConfigProperties;
  }

  @EventListener
  public void onApplicationReadyEvent(@NonNull final ApplicationReadyEvent event) {
    notice(log, "{} application {} started with id {}", CoreConstants.EMW7_MARKER,appConfigProperties.name(),
        appConfigProperties.instanceId()).log();
  }
}
