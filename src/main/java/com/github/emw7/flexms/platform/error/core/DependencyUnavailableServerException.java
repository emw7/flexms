package com.github.emw7.flexms.platform.error.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// TODO move to NOT rest as this is generic... maaybe integration.
public final class DependencyUnavailableServerException extends ServerRequestErrorException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final String CODE= "MVI5T";

  private static final String LABEL= "app.error.server.dependency-unavailable";

  public DependencyUnavailableServerException(@NonNull final String caller,
      @NonNull final String dependency, @Nullable final Map<String, String> params) {
    super(CODE, buildMessage(caller, dependency), LABEL, enrichParams(params, caller, dependency));
  }

  private static @NonNull String buildMessage (@NonNull final String caller,
      @NonNull final String dependency)
  {
    return String.format("call from %s to %s failed", caller, dependency);
  }

  private static Map<String, String> enrichParams (@Nullable Map<String, String> params, @NonNull final String caller,
      @NonNull final String dependency)
  {
    Map<String, String> enrichedParams= new HashMap<>(Optional.ofNullable(params).orElse(Map.of()));
    enrichedParams.put("caller", caller);
    enrichedParams.put("dependency", dependency);
    return enrichedParams;
  }

}
