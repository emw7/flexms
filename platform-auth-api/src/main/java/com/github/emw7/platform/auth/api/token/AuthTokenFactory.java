package com.github.emw7.platform.auth.api.token;

import org.springframework.lang.NonNull;

public interface AuthTokenFactory {

  AuthToken get(@NonNull final String stringRepresentation, final long expireWhen);
}
