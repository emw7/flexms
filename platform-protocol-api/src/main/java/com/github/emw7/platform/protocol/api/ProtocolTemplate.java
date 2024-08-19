package com.github.emw7.platform.protocol.api;


import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.discovery.api.model.Server;
import org.springframework.core.ParameterizedTypeReference;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;

/**
 * This is the interface that must be implements by protocol implementation that are used by
 * clients to talk with servers.
 * <p/>
 * Why this name? Because implementation of this interface will be a sort of
 * {@code RestTemplate}, {@code GrpcTemplate}, {@code FooTemplate} and as {@code RestTemplate}
 * implements RestOperation it has been decided to name this interface ProtocolTemplate.
 */
public interface ProtocolTemplate {

  /**
   * Contacts the specified server and returns the response.
   *
   * @param server server to contact
   * @param endPoint the name of the api to call (endPoint actually is and alias for api)
   * @param callerId the id of who is doing the call
   * @param protocolRequest the request payload
   * @param responseType class of the type of the response
   * @return the response
   * @param <T> type of the response
   * @throws DependencyErrorException if error is returned by contacted server of in case error
   *                                  occurs in contacting the server
   */
  <T, B> T exchange(Server server, String endPoint, String callerId,
      ProtocolRequest<B> protocolRequest, Class<T> responseType, AuthToken token) throws DependencyErrorException;
  
  /**
   * Contacts the specified server and returns the response.
   *
   * @param server server to contact
   * @param endPoint the name of the api to call (endPoint actually is and alias for api)
   * @param callerId the id of who is doing the call
   * @param protocolRequest the request payload
   * @param responseType type of the response
   * @return the response
   * @param <T> type of the response
   * @throws DependencyErrorException if error is returned by contacted server of in case error
   *                                  occurs in contacting the server
   */
  <T, B> T exchange(Server server, String endPoint, String callerId,
      ProtocolRequest<B> protocolRequest, ParameterizedTypeReference<T> responseType, AuthToken token)
      throws DependencyErrorException;

}
