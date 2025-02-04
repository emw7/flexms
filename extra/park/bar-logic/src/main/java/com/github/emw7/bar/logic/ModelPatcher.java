package com.github.emw7.bar.logic;

import org.springframework.lang.NonNull;

public interface ModelPatcher<M> {

  M patch (@NonNull final M model);
}
