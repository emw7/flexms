package com.github.emw7.demoapp.rest;

import com.github.emw7.platform.error.RequestErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestBody;

public interface DemoAppControllerRest {

  // test
  @NonNull
  Object test(@NonNull @RequestBody final Object o) throws RequestErrorException;

}
