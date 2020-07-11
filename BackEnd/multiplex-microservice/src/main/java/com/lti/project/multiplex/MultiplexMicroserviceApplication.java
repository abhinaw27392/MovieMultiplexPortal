package com.lti.project.multiplex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.lti.project.multiplex.feign")
public class MultiplexMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiplexMicroserviceApplication.class, args);
	}

}
