package com.github.emw7.examples.logtracing.logic;

import static com.github.emw7.platform.log.EventLogger.notice;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceA {

  private static final Logger log = LoggerFactory.getLogger(ExampleServiceA.class);

  @Observed(name = "ExampleServiceA#serviceAActionSAA")
  public void serviceAActionSAA (@NonNull final String src) {
    notice(log, "this is {} called from {}", "ExampleServiceA#serviceAActionSAA", src).log();
  }

}
