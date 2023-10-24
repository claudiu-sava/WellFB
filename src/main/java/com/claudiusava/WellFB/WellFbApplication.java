package com.claudiusava.WellFB;

import com.claudiusava.WellFB.model.Role;
import com.claudiusava.WellFB.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WellFbApplication {

	public static void main(String[] args) {
		SpringApplication.run(WellFbApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(RoleRepository roleRepo) {
		return (args) -> {
			Role role = new Role();
			role.setName("ROLE_ADMIN");
			roleRepo.save(role);
		};
	}
}
