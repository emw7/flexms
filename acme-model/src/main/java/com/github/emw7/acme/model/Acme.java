package com.github.emw7.acme.model;

import com.github.emw7.bar.model.Bar;
import com.github.emw7.foo.model.Foo;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;
import org.springframework.lang.NonNull;

public record Acme(@NonNull Foo foo, @NonNull Set<Bar> bars) {

  public Acme {
    bars= ( bars == null ) ? Collections.emptySet() : bars ;
  }

}
