package com.lti.project.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.lti.project.movie.feign")
public class MovieMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieMicroserviceApplication.class, args);
	}

}
