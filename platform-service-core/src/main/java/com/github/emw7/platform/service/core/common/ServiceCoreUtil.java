package com.github.emw7.platform.service.core.common;

import java.util.Locale;
import org.springframework.lang.NonNull;

public final class ServiceCoreUtil {

  //region API

  /**
   * Returns the name of the provided class converted to lowercase.
   * <p>
   * To be used for defining the {@code RESOURCE_NAME} public constants of models:
   * <pre>
   * public record MyModel (...) {
   *   public static final String RESOURCE_NAME= ServiceCoreUtil.resourceName(MyModel.class);
   * }
   * </pre>
   *
   * @param clazz the class for which the resource name alias is wanted
   *
   * @return the name of the provided class converted to lowercase
   *
   * @param <T> the type of the provided class
   */
  public static <T> @NonNull String resourceName (@NonNull final Class<T> clazz) {
    return clazz.getName().toLowerCase(Locale.ROOT);
  }

  //endregion API

  //region Constructors
  // prevents instantiation.
  private ServiceCoreUtil() {
  }
  //endregion Constructors

}
