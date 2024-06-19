package com.github.emw7.platform.app.error.exception;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// TODO doc
public final class DependencyUnavailableServerException extends ServerRequestErrorException {

  public static final String CALLER_KEY = "caller";
  public static final String DEPENDENCY_KEY = "dependency";

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("MVI5T");

  private static final String LABEL = appServerErrorLabel("dependency-unavailable");

  public DependencyUnavailableServerException(@Nullable Throwable cause, @NonNull final Id id,
      @NonNull final String caller, @NonNull final String dependency,
      @Nullable final Map<String, Object> params) {
    super(cause, CODE, id,
        new Error(LABEL, enrichParams(params, CALLER_KEY, caller, DEPENDENCY_KEY, dependency)));
  }

}
