package com.example.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservationApplication {


	public static void main(String[] args) {
		//System.setProperty("tomcat.useApr", "false");
		SpringApplication.run(ReservationApplication.class, args);
	}

}
