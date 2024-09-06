package com.github.emw7.platform.service.core.request.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface CallerRetriever {

  @Nullable
  Caller retrieve(@NonNull final Object context);

}
