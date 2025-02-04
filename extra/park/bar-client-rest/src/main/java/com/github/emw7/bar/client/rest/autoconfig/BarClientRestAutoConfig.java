package com.github.emw7.bar.client.rest.autoconfig;

import com.github.emw7.bar.client.BarClient;
import com.github.emw7.bar.client.rest.BarRestClient;
import com.github.emw7.bar.client.rest.DefaultBarClientRest;
import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.auth.api.token.SimpleAuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.rest.RestProtocolOperation;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestOperations;

@AutoConfiguration
public class BarClientRestAutoConfig {

  // TODO only for debug, to be removed.
  @ConditionalOnProperty(name = "com.github.emw7.bar.client.rest", havingValue = "default")
  @Bean
  public BarClient barClientRestDefault ()
  {
    return new DefaultBarClientRest();
  }

  @ConditionalOnProperty(name = "com.github.emw7.bar.client.rest", havingValue = "emw7")
  @Bean
  public BarClient barClientRest (@NonNull final RestProtocolOperation restOperations,
      @Value("${spring.application.name}") @NonNull final String appName)
  {
    return new BarRestClient(restOperations,
        // TODO this is only for debug... in production environment the Authz is
        //  provided.
        new Authz() {
          @Override
          public AuthToken authorize() {
            return new SimpleAuthToken("123", TimeUnit.MINUTES.toMillis(60));
          }
        },
        // TODO this is only for debug... in production environment the ServerRegistryDiscover is
        //  provided.
        new ServerRegistryDiscover() {
          @NonNull
          @Override
          public Server discover(@NonNull final String serviceName,
              @NonNull final String serviceVersion) throws ServerNotFoundException {
            //throw new ServerNotFoundException(new Id("95VFF"), serviceName, serviceVersion);
            return new Server("bar-rest", "v1", "http://localhost:7002");
          }
        },

        "bar-rest", "v1", appName);
  }

}
