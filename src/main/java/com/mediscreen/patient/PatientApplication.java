package com.mediscreen.patient;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PatientApplication implements CommandLineRunner {


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	

	public static void main(String[] args) {
		SpringApplication.run(PatientApplication.class, args);
	}


	@Override
	public void run(String... args) {

		System.out.println(" ");
		System.out.println("  _____                           ");
		System.out.println(" |  __ \\     _   _            _   ");
		System.out.println(" | |__) |_ _| |_(_) ___ _ __ | |_ ");
		System.out.println(" |  ___/ _` | __| |/ _ \\ '_ \\| __|");
		System.out.println(" | |  | (_| | |_| |  __/ | | | |_ ");
		System.out.println(" |_|   \\__,_|\\__|_|\\___|_| |_|\\__|");
		System.out.println(" =================================");
		System.out.println(" :: API ::                (v1.0.0)");
		System.out.println(" ");
	}
}
