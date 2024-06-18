package mdd_api.mdd_api.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import mdd_api.mdd_api.services.SubscriptionService;

@SpringBootTest
@AutoConfigureMockMvc
public class SubscriptionControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;
    
    @Test
    @WithMockUser(username="test@example.com")
    public void testSubscribe_success() throws Exception {
    	       
         Long topicId = 1L;

         doNothing().when(subscriptionService).subscribe("test@example.com", topicId);

         mockMvc.perform(post("/subscription/subscribe/{topicId}", topicId))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.message").value("Ok"));

         verify(subscriptionService, times(1)).subscribe("test@example.com", topicId);
    }
    
    @Test
    @WithMockUser(username="test@example.com")
    public void testUnsubscribe_success() throws Exception {
    	
    	Long subscriptionId = 1L;

        doNothing().when(subscriptionService).unsubscribe(subscriptionId, "test@example.com");

        mockMvc.perform(delete("/subscription/unsubscribe/{subId}", subscriptionId))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.message").value("Ok"));

        verify(subscriptionService, times(1)).unsubscribe(subscriptionId, "test@example.com");
    	
    }
    
    

}
