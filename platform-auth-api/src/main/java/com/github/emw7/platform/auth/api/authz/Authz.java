package com.github.emw7.platform.auth.api.authz;

import com.github.emw7.platform.auth.api.token.AuthToken;

/**
 * AuthoriZation.
 */
public interface Authz {

  AuthToken authorize();

}
