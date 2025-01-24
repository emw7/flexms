package com.github.emw7.platform.discovery.api;

/**
 * Interface to be implemented by who wants acting as server registry by registering servers and
 * as service discovery by searching for requested servers.
 */
public interface ServerRegistry extends ServerRegistryDiscover, ServerRegistryRegister {


}
