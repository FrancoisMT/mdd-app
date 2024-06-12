package mdd_api.mdd_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.services.JWTService;

@RestController
public class AuthController {
	
	 private final JWTService jwtService;
	 
	 public AuthController(JWTService jwtService) {
	        this.jwtService = jwtService;
	 }

	 	 
}
