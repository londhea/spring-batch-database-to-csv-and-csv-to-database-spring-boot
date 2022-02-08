package com.mfsa.extract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DatabaseToCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseToCsvApplication.class, args);
	}

}
