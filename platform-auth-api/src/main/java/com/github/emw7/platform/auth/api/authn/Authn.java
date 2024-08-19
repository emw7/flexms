package com.github.emw7.platform.auth.api.authn;

import com.github.emw7.platform.auth.api.token.SimpleAuthToken;

/**
 * AutheNtication.
 */
public interface Authn {

  SimpleAuthToken authenticate();

}
