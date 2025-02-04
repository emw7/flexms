package com.github.emw7.bar.logic.api;

import org.springframework.lang.NonNull;

public interface ModelPatcher<M> {

  M patch (@NonNull final M model);
}
