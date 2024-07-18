package mdd_api.mdd_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.repositories.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	public UserRepository userRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
		
			User user = userRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setUsername(user.getName());
			
			return ResponseEntity.ok().body(userDto);
	}
	
	
	
}
