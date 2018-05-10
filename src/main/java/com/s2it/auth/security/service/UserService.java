package com.s2it.auth.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s2it.auth.security.entity.User;
import com.s2it.auth.security.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(this.repository.findByEmail(email));
	}

}
