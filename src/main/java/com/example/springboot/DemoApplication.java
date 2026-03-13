package com.example.springboot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.springboot.entity.Product;
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
			
			logger.info("--------------------------------");

			logger.info("Add products to user with id 1");
			User user = repository.findByName("Rahul").stream().findFirst().orElse(null);

			if(user != null) {
				Product product1 = new Product(null, "Product 1");
				Product product2 = new Product(null, "Product 2");
				product1.setUser(user);
				product2.setUser(user);
				user.setProducts(List.of(product1, product2));

				repository.save(user);

			}

			
		};
	}
}
