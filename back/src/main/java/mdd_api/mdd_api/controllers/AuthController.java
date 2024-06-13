package mdd_api.mdd_api.controllers;

import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.payload.request.LoginRequest;
import mdd_api.mdd_api.payload.request.SignupRequest;
import mdd_api.mdd_api.payload.response.JwtResponse;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.AuthService;
import mdd_api.mdd_api.services.JWTService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	 private final JWTService jwtService;
	 private final AuthService authService;
	 private final PasswordEncoder passwordEncoder;
	 
	 public AuthController(JWTService jwtService, AuthService authService, PasswordEncoder passwordEncoder) {
	        this.jwtService = jwtService;
	        this.authService = authService;
	        this.passwordEncoder = passwordEncoder;
	 }

	 @PostMapping("/register")
	 @Operation(summary = "Register a new user", description = "This operation registers a new user and returns a jwt token.")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "User registered successfully!", 
	    		 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content)
	 })
	 public ResponseEntity<MessageResponseHandler> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws Exception {
		 
		 User user = new User();
		 user.setMail(signUpRequest.getEmail());
		 user.setName(signUpRequest.getUsername());
		 user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		 
		 authService.register(user);
		 		 
		 return ResponseEntity.ok(new MessageResponseHandler("User registered successfully!"));
		 
	 }
	 
	 @PostMapping("/login")
	 @Operation(summary = "Log in a user", description = "This operation logs in a user and returns a jwt token.")
	    @ApiResponses(value = { 
	        @ApiResponse(responseCode = "200", description = "User logged in successfully", 
	        		content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = JwtResponse.class))),
	        @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content), 
	        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
	 })
	 public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
				 
		 User authenticatedUser = authService.login(loginRequest);	
		 String jwtToken = jwtService.generateToken(authenticatedUser);
		 
		 return ResponseEntity.ok(new JwtResponse(
				 authenticatedUser.getId(),
				 authenticatedUser.getUsername(),
				 authenticatedUser.getMail(),
				 jwtToken
		));
		 
	 }
	 
	 @PutMapping("/update")
	 @Operation(summary = "Update user credentials")
	 @ApiResponses(value = { 
		        @ApiResponse(responseCode = "200", description = "credentials successfully updated", 
		        		content = @Content(mediaType = "application/json",
		                 schema = @Schema(implementation = JwtResponse.class))),
		        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content), 
		        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content), 
		        @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content)
	 })
	 public ResponseEntity<JwtResponse> updateCredentials(@Valid @RequestBody SignupRequest request) {
		 
		 User user = new User();
		 user.setMail(request.getEmail());
		 user.setName(request.getUsername());
		 user.setPassword(passwordEncoder.encode(request.getPassword()));
		 
		 User updatedUser = authService.updateCredentials(user);
		 String jwtToken = jwtService.generateToken(updatedUser);
		 
		 return ResponseEntity.ok(new JwtResponse(
				 updatedUser.getId(),
				 updatedUser.getUsername(),
				 updatedUser.getMail(),
				 jwtToken
		)); 

	 }
	 
	 	 
}
