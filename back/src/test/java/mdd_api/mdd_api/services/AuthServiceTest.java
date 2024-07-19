package mdd_api.mdd_api.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.payload.request.LoginRequest;
import mdd_api.mdd_api.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class AuthServiceTest {

	 @Mock
     private UserRepository userRepository;
	 
	 @Mock
	 private AuthenticationManager authenticationManager;
	 
	 @Mock
	 private Authentication authentication;
	 
	 @Mock
	 private UserDetailsService userDetailsService;
	 
	 @InjectMocks
	 private AuthService authService;
	 
	 public User user;
	 public User user2;
	 public LoginRequest loginRequest;
	 	 
	 @BeforeEach
	 void setUp() {
	     
		 MockitoAnnotations.openMocks(this);
		 			
		 user = new User();
		 user.setName("testuser");
		 user.setMail("user@test.fr");
		 user.setPassword("password");
		 
		 user2 = new User();
		 user2.setMail("test@live.fr");
		 user2.setPassword("TestPwd_88");
		 
		 loginRequest = new LoginRequest();
		 loginRequest.setEmail("user@test.fr");
		 loginRequest.setPassword("password");

	 }
	 
	 @AfterEach
	 void tearDown() throws Exception {
		 this.user = null;
		 this.loginRequest = null;
	 }
	 
	 @Test
	 public void testRegister_alreadyExistingMail_shouldReturnNotFound() {
		 		 
		 // Arrange	
		 when(userRepository.existsByMail(this.user.getMail())).thenReturn(true);

		 // Act
		 CustomException exception = assertThrows(CustomException.class, () -> {
		        authService.register(this.user);
		 });

		 // Assert
		 assertEquals("Email already in use", exception.getMessage());
		 assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
		 verify(userRepository, never()).save(any());

	 }
	 
	 @Test
	 public void testRegister_success() {
		 
		 when(userRepository.existsByMail(user.getMail())).thenReturn(false);
		 when(userRepository.save(any(User.class))).thenReturn(user);
		 
	     authService.register(user);

	     verify(userRepository, times(1)).save(user);
		 
	 }
	 
	 @Test
	 public void testLogin_success() {
		 

		 
		 // Arrange
		 when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
		 		.thenReturn(mock(Authentication.class));
		 when(userRepository.findByMail(loginRequest.getEmail())).thenReturn(Optional.of(user));

		 // Act
		 User loggedInUser = authService.login(user);

		 // Assert
		 assertEquals(user.getMail(), loggedInUser.getMail());
		 
	 }
	 
	 @Test
	 public void testLogin_invalidCredentials() {
	 
		 // Arrange
	     when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
	                .thenThrow(new BadCredentialsException("Invalid Credentials"));

	     // Act & Assert
	     CustomException exception = assertThrows(CustomException.class, () -> {
	            authService.login(user2);
	     });

	     assertEquals("Invalid Credentials", exception.getMessage());
	        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
	    
	 }

	 @Test
	 public void testLogin_userNotFound() {
	 
		 // Arrange
	     when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
	               .thenReturn(mock(Authentication.class));
	     when(userRepository.findByMail(loginRequest.getEmail())).thenReturn(Optional.empty());

	     // Act & Assert
	     CustomException exception = assertThrows(CustomException.class, () -> {
	            authService.login(user2);
	     });

	     assertEquals("User not found", exception.getMessage());
	     assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	
	 }
	 
	 
	 @Test
	 public void testUpdateCredentials_success() {
		 
		 Authentication authentication = mock(Authentication.class);
		 SecurityContext securityContext = mock(SecurityContext.class);
	     when(securityContext.getAuthentication()).thenReturn(authentication);
	     SecurityContextHolder.setContext(securityContext); 

	     // Mock comportement de l'authentification
	     when(authentication.isAuthenticated()).thenReturn(true);
	     when(authentication.getName()).thenReturn("authenticated_user@test.com");

	     // Arrange
	     User updatedUser = new User();
	     updatedUser.setId(1L);
	     updatedUser.setName("updateduser");
	     updatedUser.setMail("updated@test.fr");
	     updatedUser.setPassword("newpassword");

	     User currentUser = new User();
	     currentUser.setId(1L);
	     currentUser.setName("existinguser");
	     currentUser.setMail("authenticated_user@test.com");
	     currentUser.setPassword("password123");

	     // Mock comportement du repository
	     when(userRepository.findByMail(currentUser.getMail())).thenReturn(Optional.of(currentUser));
	     when(userRepository.existsByMail(updatedUser.getMail())).thenReturn(false);
	     when(userRepository.save(currentUser)).thenReturn(updatedUser);

	     // Act
	     User updated = authService.updateCredentials(updatedUser);

	     // Assert
	     assertNotNull(updated); 
	     assertEquals(updatedUser.getId(), updated.getId());
	     assertEquals(updatedUser.getName(), updated.getName());
	     assertEquals(updatedUser.getMail(), updated.getMail());
	     assertEquals(updatedUser.getPassword(), updated.getPassword());

	     verify(userRepository).findByMail("authenticated_user@test.com");
	     verify(userRepository).existsByMail("updated@test.fr");
	     verify(userRepository).save(currentUser);
	 
	 }
	 
	 
}
