package com.github.emw7.examples.updownstream.downstream.client.rest.autoconfig;

import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient;
import com.github.emw7.examples.updownstream.downstream.client.rest.SensorClientRest;
import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.service.client.rest.ClientRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;

@AutoConfiguration
public class DownstreamRestAutoConfig {

  @Bean
  public SensorClient sensorClientRest(@NonNull final ProtocolTemplate protocolTemplate,
      @NonNull final Authz authz, @NonNull final ServerRegistryDiscover discover,
      @Value("${spring.application.name}") @NonNull final String callerId,
      @NonNull final ClientRest clientRest) {
    return new SensorClientRest(clientRest/*protocolTemplate, authz, discover, callerId*/);
  }
}
