package com.github.emw7.platform.auth.api.token;

/**
 * A generic representation of a token either authoriZation or autheNtication
 */
public interface AuthToken {

  /**
   * Returns the string representation of the token.
   * <p/>
   * This is different from {@code toString()} in the mean that {@code toString} returns a
   * representation of the object instance that could be different in case two objects represent
   * the same token, while this method returns the string representation of the token itself and
   * must be equal for two objects that represent the same token; refer to the following examples:
   * <pre>
   *   token is Abc#123
   *   object-1 toString could be [instance-1] token: 'Abc#123'
   *   object-2 toString could be [instance-2] token: 'Abc#123'
   *   stringRepresentation of both object-1 and object-2 is Abc#123
   * </pre>
   *
   * @return the string representation of the token
   */
  String stringRepresentation();

  /**
   * Returns whether the token is expired or not.
   * @return whether the token is expired or not.
   */
  boolean isExpired();

}
