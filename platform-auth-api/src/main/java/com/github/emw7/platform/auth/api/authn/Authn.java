package com.github.emw7.platform.auth.api.authn;

import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.auth.api.token.SimpleAuthToken;

/**
 * AutheNtication.
 */
public interface Authn {

  AuthToken authenticate();

}
