package com.s2it.auth.security.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class JwtAuthenticationDTO {

	@NotEmpty(message = "Como que você vai logar sem email estag?")
	@Email(message = "Opa, ta querendo me hackear?.")
	private String email;

	@NotEmpty(message = "Como que você vai logar sem senha estag?")
	private String password;

}
