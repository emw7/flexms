package com.github.emw7.flexms.commons.acme;

import java.time.ZonedDateTime;

public record Foo (ZonedDateTime d, String uuid, String name, int i) {

  public Foo {
    d= ( d == null ) ? ZonedDateTime.now() : d;
  }

  public Foo(final String uuid, final String name, final int i) {
    this(ZonedDateTime.now(),uuid,name,i);
  }
}
