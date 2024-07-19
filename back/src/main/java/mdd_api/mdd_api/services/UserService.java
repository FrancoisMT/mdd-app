package mdd_api.mdd_api.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.repositories.UserRepository;

@Service
public class UserService {

	public UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getUser(String mail) {
		
		User user = userRepository.findByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + mail));
		
		return user;
	}
	
}
