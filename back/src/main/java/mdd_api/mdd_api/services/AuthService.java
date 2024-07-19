package mdd_api.mdd_api.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.repositories.UserRepository;

@Service
public class AuthService {
	
	 private final UserRepository userRepository;
	 private final AuthenticationManager authenticationManager;
	 
	 public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager) {
		 this.userRepository = userRepository;
		 this.authenticationManager = authenticationManager;
	 }

	 public void register(User user) {
			
		if (userRepository.existsByMail(user.getMail())) {
		     throw new CustomException("Email already in use", HttpStatus.CONFLICT);
		}
			
		userRepository.save(user);
	 }
	 
	 public User login(User user) {
			
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword()));			
						
		return userRepository.findByMail(user.getMail())
					.orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));			 
	 }
	 
	 public User updateCredentials(User updatedUser) {
		
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			 
	 	if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException("User not authenticated", HttpStatus.BAD_REQUEST);
	 	}
			 
	 	String email = authentication.getName();
	 	User currentUser = userRepository.findByMail(email)
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
			 
	 	/* User has updated his email : we have to check if the new email is already in use for another user */
	 	if (!currentUser.getMail().equals(updatedUser.getMail())) {
               if (userRepository.existsByMail(updatedUser.getMail())) {
                  	 	throw new CustomException("Email already in use", HttpStatus.CONFLICT);
	           }
		}
			 
		currentUser.setName(updatedUser.getName());
		currentUser.setMail(updatedUser.getMail());
		currentUser.setPassword(updatedUser.getPassword());
	         
		return userRepository.save(currentUser); 
	 }
	 
	 
	 
	
}
