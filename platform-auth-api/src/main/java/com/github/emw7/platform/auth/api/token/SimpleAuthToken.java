package com.github.emw7.platform.auth.api.token;

/**
 * A simple auth token.
 */
public final class SimpleAuthToken implements AuthToken {

  private final String stringRepresentation;
  private final long expiresWhen;

  /**
   * @param stringRepresentation the string representation of the stringRepresentation as will be
   *                             returned by {@link #stringRepresentation()}
   * @param expiresWhen          when the stringRepresentation expired (epoch) with milliseconds;
   *                             will be compared with {@link System#currentTimeMillis()} by
   *                             {@link #isExpired()}
   */
  public SimpleAuthToken(String stringRepresentation, long expiresWhen) {
    this.stringRepresentation = stringRepresentation;
    this.expiresWhen = expiresWhen;
  }

  //region API

  /**
   * Returns the token representation.
   * <p/>
   * The token representation is the #stringRepresentation with which the object was constructed.
   *
   * @return the token representation
   */
  @Override
  public String stringRepresentation() {
    return stringRepresentation;
  }

  /**
   * Returns whether the token is expired.
   * <p/>
   * The token is expired if {@link #expiresWhen} with which the object was constructed is lesser
   * than {@link System#currentTimeMillis()}
   *
   * @return whether the token is expired or not.
   */
  @Override
  public boolean isExpired() {
    return getExpiresWhen() < System.currentTimeMillis();
  }
  //endregion API

  //region Getters & Setters
  private long getExpiresWhen() {
    return expiresWhen;
  }
  //endregion Getters & Setters

}
