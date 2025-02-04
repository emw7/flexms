package com.github.emw7.platform.service.core.runtime;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface ApiDefinition {

  enum ApiSemantic {
    POST, GET, PUT, PATCH, DELETE
  }

  @NonNull ApiSemantic semantic ();

  @NonNull String endpoint ();

  @Nullable Class<?> requestBodyClazz ();

  @NonNull List<String> pathParameters ();

  @NonNull List<String> queryParameters ();

}
