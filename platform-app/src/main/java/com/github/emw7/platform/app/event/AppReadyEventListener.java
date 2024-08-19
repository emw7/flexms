package com.github.emw7.platform.app.event;

import static com.github.emw7.platform.log.EventLogger.notice;

import com.github.emw7.platform.app.config.AppConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;


public final class AppReadyEventListener {

  private static final Logger logger = LoggerFactory.getLogger(AppReadyEventListener.class);

  private final AppConfigProperties appConfigProperties;

  public AppReadyEventListener(@NonNull final AppConfigProperties appConfigProperties) {
    this.appConfigProperties = appConfigProperties;
  }

  @EventListener
  public void onApplicationReadyEvent(@NonNull final ApplicationReadyEvent event) {
    // FIXME either notice or info.
    notice(logger,"{} application {} started with id {}","[EMW7]",appConfigProperties.name(),
        appConfigProperties.instanceId());
  }
}
