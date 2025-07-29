package com.github.emw7.examples;

import com.github.emw7.examples.api.ExampleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// implements CommandLineRunner comes from https://www.baeldung.com/spring-boot-console-app.
@SpringBootApplication
public class ExampleApplication implements CommandLineRunner {

	@Autowired
	private ExampleController exampleController;

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		exampleController.actionA("main");
		exampleController.actionB("main");
		exampleController.actionC("main");
	}
}
