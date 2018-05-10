package com.s2it.auth.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class ProtectedController {

	@GetMapping(value = "/{name}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String exemplo(@PathVariable("name") String name) {
		return "Koe " + name;
	}
	
}
