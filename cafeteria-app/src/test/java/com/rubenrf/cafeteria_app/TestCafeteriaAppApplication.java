package com.rubenrf.cafeteria_app;

import org.springframework.boot.SpringApplication;

public class TestCafeteriaAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(CafeteriaAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
