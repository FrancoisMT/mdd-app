package mdd_api.mdd_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;

import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.services.PostService;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PostService postService;
	
	
    @Test
    @WithMockUser
    public void testCreate_success() throws Exception {
    	
        PostDto request = new PostDto();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setTopicId(2L);
        request.setUserId(1L);
        
        mockMvc.perform(post("/posts/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post successfully created"));
    }
    
    @Test
    @WithMockUser
    public void testGetAll_success() throws Exception {

        UserDto user = new UserDto();
    	user.setId(1L);
    	user.setUsername("Test");
    	
    	Topic topic = new Topic();
    	topic.setId(1L);
    	topic.setTitle("Title Topic");
    	topic.setDescription("Description Topic");
    	
    	PostResponseDto post1 = new PostResponseDto();
        post1.setId(1L);
        post1.setTitle("First Post");
        post1.setDescription("Description post 1");
        post1.setTopic(topic);
        post1.setUser(user);

        PostResponseDto post2 = new PostResponseDto();
        post2.setId(2L);
        post2.setTitle("Second Post");
        post2.setDescription("Description post 2");
        post2.setTopic(topic);
        post2.setUser(user);
       
        mockMvc.perform(get("/posts/all")
                .contentType("application/json"))
                .andExpect(status().isOk());
          	    	
    }
	
}
