package com.github.emw7.foo.model;

import java.time.ZonedDateTime;

// i is put here only to usa a primitive type.
public record Foo (String id, String name, int i, ZonedDateTime creationTime) {

  public Foo {
    creationTime = ( creationTime == null ) ? ZonedDateTime.now() : creationTime;
  }

  public Foo(final String id, final String name, final int i) {
    this(id,name,i,ZonedDateTime.now());
  }

  public static final String RESOURCE_NAME= Foo.class.getName();

}
