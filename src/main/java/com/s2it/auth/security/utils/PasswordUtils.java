package com.s2it.auth.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	/**
	 * Gera um hash utilizando o BCrypt.
	 * 
	 * @param password
	 * @return String
	 */
	public static String generateBCrypt(String password) {
		if (password == null) {
			return password;
		}

		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.encode(password);
	}

	/**
	 * Verifica se a senha é válida.
	 * 
	 * @param password
	 * @param encodedPassword
	 * @return boolean
	 */
	public static boolean validatePassword(String password, String encodedPassword) {
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.matches(password, encodedPassword);
	}

}