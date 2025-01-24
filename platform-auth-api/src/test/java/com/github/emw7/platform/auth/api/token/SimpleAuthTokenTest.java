package com.github.emw7.platform.auth.api.token;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleAuthTokenTest {

  @Test
  public void test_string_representation () {
    final SimpleAuthToken sut= new SimpleAuthToken("thiS/is-The_toKen@STRING:representation0",
        System.currentTimeMillis());
    Assertions.assertThat(sut.stringRepresentation())
        .as("token string representation is equal to the one provided to the constructor")
        .isEqualTo("thiS/is-The_toKen@STRING:representation0");
  }

  @Test
  public void test_expired () {
    final SimpleAuthToken sut= new SimpleAuthToken("thiS/is-The_toKen@STRING:representation0",
        System.currentTimeMillis() - 1);
    Assertions.assertThat(sut.isExpired())
        .as("token is expired as created with expiration in the past")
        .isEqualTo(true);
  }

  @Test
  public void test_not_expired () {
    final SimpleAuthToken sut= new SimpleAuthToken("thiS/is-The_toKen@STRING:representation0",
        System.currentTimeMillis() + Duration.ofMinutes(1).toMillis());
    Assertions.assertThat(sut.isExpired())
        .as("token is not expired as created to expired in the future (1 minute ahead)")
        .isEqualTo(false);
  }

}
