package com.github.emw7.flexms.platform.server.event;

import com.github.emw7.flexms.platform.server.AppConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;


public final class ApplicationReadyEventListener {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

  private final AppConfigProperties appConfigProperties;

  public ApplicationReadyEventListener(final AppConfigProperties appConfigProperties) {
    this.appConfigProperties = appConfigProperties;
  }

  @EventListener
  public void onApplicationReadyEvent (@NonNull final ApplicationReadyEvent event) {
    logger.info("[EMW7] application {} started with id {}",appConfigProperties.name(), appConfigProperties.instanceId());
  }
}
