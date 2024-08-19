package com.github.emw7.foo.logic;

import org.springframework.lang.NonNull;

public interface ModelPatcher<M> {

  M patch (@NonNull final M model);
}
