package com.s2it.auth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.s2it.auth.security.entity.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
