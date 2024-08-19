package com.github.emw7.platform.auth.api.token;

import org.springframework.lang.NonNull;

public class SimpleAuthTokenFactory implements AuthTokenFactory {

  @Override
  public AuthToken get(@NonNull final String stringRepresentation, final long expireWhen) {
    return new SimpleAuthToken(stringRepresentation, expireWhen);
  }

}
