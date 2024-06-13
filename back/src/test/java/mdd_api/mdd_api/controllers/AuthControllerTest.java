package mdd_api.mdd_api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.payload.request.LoginRequest;
import mdd_api.mdd_api.payload.request.SignupRequest;
import mdd_api.mdd_api.services.AuthService;
import mdd_api.mdd_api.services.JWTService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
    @MockBean 
    private AuthService authService;
    @MockBean
    private JWTService jwtService;
    @Autowired
    private ObjectMapper objectMapper;
	 
    @Test
    public void testRegistration_nullBody_shouldReturnBadRequest() throws Exception {
		
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) 
                .andExpect(status().isBadRequest());
        
    }
    
    @Test
    public void testRegisterUser() throws Exception {
        
    	SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("passworduser");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
        
    }
    
    @Test
    public void testLogin_success() throws Exception {
    	
       LoginRequest loginRequest = new LoginRequest();
       loginRequest.setEmail("user@test.fr");
       loginRequest.setPassword("passworduser");

       User mockUser = new User();
       mockUser.setId(1L);
       mockUser.setName("testuser");
       mockUser.setMail("user@test.fr");

       String mockJwtToken = "mockJwtToken";

       // Mocking the behavior of AuthService and JWTService
       when(authService.login(any(LoginRequest.class))).thenReturn(mockUser);
       when(jwtService.generateToken(any(User.class))).thenReturn(mockJwtToken);
       
       mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(loginRequest)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(mockUser.getId()))
              .andExpect(jsonPath("$.username").value(mockUser.getUsername()))
              .andExpect(jsonPath("$.mail").value(mockUser.getMail()))
              .andExpect(jsonPath("$.token").value(mockJwtToken));
    
    }
    
    @Test
    @WithMockUser
    public void testUpadateCrendentials_success() throws Exception {
    	
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("passworduser");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("updatedUser");
        updatedUser.setMail("updated@example.com");

        String mockJwtToken = "mockJwtToken";

        when(authService.updateCredentials(any(User.class))).thenReturn(updatedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(mockJwtToken);

        mockMvc.perform(put("/auth/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.mail").value(updatedUser.getMail()))
                .andExpect(jsonPath("$.token").value(mockJwtToken));
    }
    
    
    
    
    
  
    

	 
	 
	 
	 
	 
	 
	 
	
}
