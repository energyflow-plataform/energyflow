package com.pi.energyflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class EnergyflowApplication {

	public static void main(String[] args) {
		
		try {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			System.out.println("Running without .env file - using environment variables");
		}
		
		SpringApplication.run(EnergyflowApplication.class, args);
	}

}
