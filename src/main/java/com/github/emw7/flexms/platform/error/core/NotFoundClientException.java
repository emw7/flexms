package com.github.emw7.flexms.platform.error.core;

import com.github.emw7.flexms.platform.error.core.category.NotFound;
import java.util.Map;

@NotFound
public final class NotFoundClientException extends ClientRequestErrorException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final String CODE= "D4FAA";

  public NotFoundClientException(final String message,
      final String label,
      final Map<String, String> params) {
    super(CODE, message, label, params);
  }
}
