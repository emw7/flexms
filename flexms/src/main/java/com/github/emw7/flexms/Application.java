package com.github.emw7.flexms;

import static com.github.emw7.platform.log.EventLogger.*;
import com.github.emw7.platform.log.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class Application {

	private static final Logger log= LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		LogEvent logEvent= doing(log, "launch application");
		done(logEvent);

		SpringApplication.run(Application.class, args);
	}

}
