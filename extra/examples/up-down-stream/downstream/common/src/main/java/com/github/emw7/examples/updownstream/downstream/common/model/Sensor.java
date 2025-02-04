package com.github.emw7.examples.updownstream.downstream.common.model;

import com.github.emw7.platform.service.core.common.ServiceCoreUtil;
import org.springframework.lang.NonNull;

/**
 * The sensor model.
 *
 * @param code
 * @param name
 * @param type
 */
public record Sensor(@NonNull String code, @NonNull String name, int type) {

  // A model SHOULD always define a public constant in such a way.
  public static final String RESOURCE_NAME= ServiceCoreUtil.resourceName(Sensor.class);

}
