package com.github.emw7.flexms.platform.error.core;

import java.util.Map;

public final class BadPropertiesClientException extends BadRequestClientException {

  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final String CODE= "FBTYV";

  public BadPropertiesClientException(final String message,
      final String label,
      final Map<String, String> params) {
    super(CODE, message, label, params);
  }
}
