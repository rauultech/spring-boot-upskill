package com.example.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {
	private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			repository.save(new User(null, "Rahul"));
			repository.save(new User(null, "John"));
			repository.save(new User(null, "Jane"));
			logger.info("Users found with findAll():");
			logger.info("-------------------------------");
			for (var user : repository.findAll()) {
				logger.info(user.toString());
			}
			logger.info("User found with findByName('Rahul'):");
			logger.info("--------------------------------");
			logger.info(repository.findByName("Rahul").toString());
			logger.info("User found with findById(1L):");
			logger.info("--------------------------------");
			logger.info(repository.findById(1L).toString());
		};
	}
}
