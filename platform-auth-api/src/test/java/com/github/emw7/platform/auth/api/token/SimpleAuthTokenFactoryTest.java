package com.github.emw7.platform.auth.api.token;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleAuthTokenFactoryTest {

  @Test
  void test_get_expired() {
    final SimpleAuthTokenFactory sut= new SimpleAuthTokenFactory();
    final AuthToken token= sut.get("thiS/is-The_toKen@STRING:representation0", System.currentTimeMillis() - 1);
    Assertions.assertThat(token.stringRepresentation())
        .as("constructed token string representation is equal to the one provided to the constructor")
        .isEqualTo("thiS/is-The_toKen@STRING:representation0");
    Assertions.assertThat(token.isExpired())
        .as("constructed token is expired as created with expiration in the past")
        .isEqualTo(true);
  }

  @Test
  void test_get_not_expired() {
    final SimpleAuthTokenFactory sut= new SimpleAuthTokenFactory();
    final AuthToken token= sut.get("thiS/is-The_toKen@STRING:representation0",
        System.currentTimeMillis() + Duration.ofMinutes(1).toMillis());
    Assertions.assertThat(token.stringRepresentation())
        .as("constructed token string representation is equal to the one provided to the constructor")
        .isEqualTo("thiS/is-The_toKen@STRING:representation0");
    Assertions.assertThat(token.isExpired())
        .as("constructed token is expired as created with expiration in the past")
        .isEqualTo(false);
  }

}