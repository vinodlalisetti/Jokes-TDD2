package com.example.jokes;

import org.springframework.boot.SpringApplication;

public class TestJokesApplication {

	public static void main(String[] args) {
		SpringApplication.from(JokesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
