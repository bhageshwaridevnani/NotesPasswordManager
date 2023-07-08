package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import java.io.IOException;
import java.net.URISyntaxException;

@EnableMongoAuditing
@SpringBootApplication
@EnableAsync
public class DemoApplication {

	public static void main(String[] args) throws URISyntaxException, IOException {
		SpringApplication.run(DemoApplication.class, args);

	}

}
