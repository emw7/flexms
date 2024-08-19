package com.github.emw7.platform.discovery.api.model;

import org.springframework.lang.NonNull;

/**
 * Server information.
 *
 * @param name    server name
 * @param version server version
 * @param url     the url at which the server answers
 */
public record Server(@NonNull String name, @NonNull String version,
                     @NonNull String url) {

}
