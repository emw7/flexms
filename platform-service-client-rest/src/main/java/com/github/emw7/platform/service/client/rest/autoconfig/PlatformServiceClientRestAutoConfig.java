package com.github.emw7.platform.service.client.rest.autoconfig;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.service.client.rest.ClientRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformServiceClientRestAutoConfig {

  @Bean
  public ClientRest clientRest (@NonNull final ProtocolTemplate protocolTemplate,
      @NonNull final Authz authz,
      @NonNull final ServerRegistryDiscover serverRegistryDiscover,
      @Value("${spring.application.name}") @NonNull final String callerId) {
    return new ClientRest(protocolTemplate, authz, serverRegistryDiscover, callerId);
  }

}
