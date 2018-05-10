package com.s2it.auth.security.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.s2it.auth.api.response.Response;
import com.s2it.auth.security.dtos.JwtAuthenticationDTO;
import com.s2it.auth.security.dtos.TokenDTO;
import com.s2it.auth.security.utils.JwtTokenUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/public/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthenticationController {

	private static final String TOKEN_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtils jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Método responsável por gerar e retornar um novo token JWT.
	 * 
	 * @param authenticationDto
	 * @param result
	 * @return ResponseEntity<Response<TokenDTO>>
	 * @throws AuthenticationException
	 */
	@PostMapping
	public ResponseEntity<Response<TokenDTO>> generateTokenJwt(
			@Valid @RequestBody JwtAuthenticationDTO authenticationDto, BindingResult result)
			throws AuthenticationException {
		Response<TokenDTO> response = new Response<TokenDTO>();

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		log.info("Gerando token para o email {}.", authenticationDto.getEmail());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
		String token = jwtTokenUtil.getToken(userDetails);
		response.setData(new TokenDTO(token));

		return ResponseEntity.ok(response);
	}

	/**
	 * Método responsável por gerar um novo token com novo prazo para expiração.
	 * 
	 * @param request
	 * @return ResponseEntity<Response<TokenDTO>>
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<Response<TokenDTO>> refreshTokenJwt(HttpServletRequest request) {
		log.info("Atualizando um token JWT.");
		Response<TokenDTO> response = new Response<TokenDTO>();
		Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

		if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
			token = Optional.of(token.get().substring(7));
		}

		if (!token.isPresent()) {
			response.getErrors().add("Token não informado.");
		} else if (!jwtTokenUtil.isValidToken(token.get())) {
			response.getErrors().add("Token inválido ou expirado.");
		}

		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}

		String refreshedToken = jwtTokenUtil.refreshToken(token.get());
		response.setData(new TokenDTO(refreshedToken));

		return ResponseEntity.ok(response);
	}

}