package com.ecourier.parcelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EcourierParcelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcourierParcelServiceApplication.class, args);
	}

}
