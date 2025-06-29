package com.ecourier.trackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EcourierTrackingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcourierTrackingServiceApplication.class, args);
	}

}
