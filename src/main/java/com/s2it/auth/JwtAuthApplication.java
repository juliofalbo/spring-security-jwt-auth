package com.s2it.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.s2it.auth.security.entity.User;
import com.s2it.auth.security.profiles.ProfileEnum;
import com.s2it.auth.security.repository.UserRepository;
import com.s2it.auth.security.utils.PasswordUtils;

@SpringBootApplication
public class JwtAuthApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {

			User estag = new User();
			estag.setEmail("estagiario@s2it.com.br");
			estag.setRole(ProfileEnum.ROLE_USER);
			estag.setPassword(PasswordUtils.generateBCrypt("123456"));
			this.userRepository.save(estag);

			User dev = new User();
			dev.setEmail("dev@s2it.com.br");
			dev.setRole(ProfileEnum.ROLE_ADMIN);
			dev.setPassword(PasswordUtils.generateBCrypt("123456"));
			this.userRepository.save(dev);

		};
	}
}
