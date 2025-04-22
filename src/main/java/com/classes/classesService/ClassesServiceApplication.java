package com.classes.classesService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClassesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassesServiceApplication.class, args);
	}

}
