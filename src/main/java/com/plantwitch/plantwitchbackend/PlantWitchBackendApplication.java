package com.plantwitch.plantwitchbackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.plantwitch.plantwitchbackend.entity", "com.plantwitch.plantwitchbackend.user"})


public class PlantWitchBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantWitchBackendApplication.class, args);
		System.out.println("Hello World!");
	}
}


