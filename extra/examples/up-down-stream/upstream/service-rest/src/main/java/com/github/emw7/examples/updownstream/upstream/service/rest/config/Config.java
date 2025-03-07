package com.github.emw7.examples.updownstream.upstream.service.rest.config;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.SimpleAuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.i18n.CompositeMessageSource;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class Config {

  /**
   * See {@code extra/doc/Internationalization/README.md}.
   */
  @Bean
  public MessageSource messageSource(Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

  @Bean
  public Authz authz() {
    // TODO this is only for debug... in production environment the Authz is
    //  provided.
    return () -> new SimpleAuthToken("123", TimeUnit.MINUTES.toMillis(60));
  }

  // TODO se qua metto (quindi senza il $)  @Value("{com.github.emw7.examples.updownstream.upstream.service.rest.conf.downstream-url:http://localhost:8701}"
  //  RestProtocol template non riesce a creare l'url e lancia eccezone illegal argument,
  //  così l'exchanger lancia un neseted runtime ex... la cui causa viene convertita senza check a DependencyErrorException,
  //  ma questo fa lanciare un'eccezione di tipo ClassCastException... che esce dalla gestione errori standard...
  //  Far rientrare nella gestione errori standard? Se sì, come?
  @Bean
  public ServerRegistryDiscover serverRegistryDiscover(
      @Value("${com.github.emw7.examples.updownstream.upstream.service.rest.conf.downstream-url:http://localhost:8701}") @NonNull final String downstreamUrl) {
    // TODO this is only for debug... in production environment the ServerRegistryDiscover is
    //  provided.
    return new ServerRegistryDiscover() {
      @NonNull
      @Override
      public Server discover(@NonNull final String serviceName,
          @NonNull final String serviceVersion) throws ServerNotFoundException {
        if (downstreamUrl.equalsIgnoreCase("not-found)")) {
          throw new ServerNotFoundException(null, serviceName, serviceVersion);
        } else {
          return new Server(serviceName, serviceVersion, downstreamUrl);
        }
      }
    };
  }

}
