package com.github.emw7.examples.serviceruntime;

import com.github.emw7.examples.serviceruntime.api.SensorControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.NonNull;

// Implements CommandLineRunner comes from https://www.baeldung.com/spring-boot-console-app.
@SpringBootApplication
public class AcmeApplication implements CommandLineRunner {

  @Autowired
  private SensorControllerImpl controllerSensor;

  public static void main(String[] args) {
    SpringApplication.run(AcmeApplication.class, args);
  }

  @Override
  public void run(final String... args) {

    switch (args[0]) {
      case "create":
        create(args);
        break;
      case "delete":
        delete(args);
        break;
    }
  }

  //region Private
  private void create(@NonNull final String... args) {
    final Object res = controllerSensor.create(args[1], args[2], Integer.parseInt(args[3]));
    System.out.printf("res: %s%n", res);
  }

  private void delete(@NonNull final String... args) {
    final Object res = controllerSensor.delete(args[1]);
    System.out.printf("res: %s%n", res);
  }
  //endregion Private

}
