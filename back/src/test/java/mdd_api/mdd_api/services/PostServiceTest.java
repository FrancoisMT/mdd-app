package mdd_api.mdd_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.repositories.PostRepository;
import mdd_api.mdd_api.repositories.SubscriptionRepository;
import mdd_api.mdd_api.repositories.TopicRepository;
import mdd_api.mdd_api.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	SubscriptionRepository subscriptionRepository;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	PostRepository postRepository;
	
	@Mock
	TopicRepository topicRepository;
	
	@InjectMocks
	PostService postService;
	
	@Test
	public void testCreate_userNotFound_shouldReturnNotFoundException() {
		
	    String mail = "user@example.com";
	    PostDto postDto = new PostDto();
	    postDto.setTopicId(1L);

	    when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

	    CustomException exception = assertThrows(CustomException.class, () -> {
	        postService.create(mail, postDto);
	    });

	    assertEquals("Error creating post: User not found with mail: " + mail, exception.getMessage());
	    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	    
	}
	
	@Test
	public void testCreate_topicNotFound_shouldReturnNotFoundException() {
		
	    String mail = "user@example.com";
	    PostDto postDto = new PostDto();
	    postDto.setTopicId(1L);

	    User user = new User();
	    when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
	    when(topicRepository.findById(postDto.getTopicId())).thenReturn(Optional.empty());

	    CustomException exception = assertThrows(CustomException.class, () -> {
	        postService.create(mail, postDto);
	    });

	    assertEquals("Error creating post: Topic not found with id: " + postDto.getTopicId(), exception.getMessage());
	    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	    
	}
	
	@Test
	public void testCreate_success() {
		
	    String mail = "user@example.com";
	    PostDto postDto = new PostDto();
	    postDto.setTopicId(1L);

	    User user = new User();
	    Topic topic = new Topic();
	    Post post = new Post();

	    when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
	    when(topicRepository.findById(postDto.getTopicId())).thenReturn(Optional.of(topic));
	    when(postRepository.save(any(Post.class))).thenReturn(post);

	    postService.create(mail, postDto);

	    verify(postRepository, times(1)).save(any(Post.class));
	    
	}
	
	
	@Test
	public void testGetAll_userNotFound_shouldReturnNotFoundException() {
		
		 String mail = "user@example.com";
	
		 when(userRepository.findByMail(mail)).thenReturn(Optional.empty());
         CustomException exception = assertThrows(CustomException.class, () -> {
		        postService.getAll(mail);
	     });

	     assertEquals("Error retrieving post: User not found with mail: " + mail, exception.getMessage());
	     assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());		
		
	}
	
	 @Test
	 public void testGetAll_Success() {
		 
		 String username = "user@example.com";
	     User user = new User();
	     user.setId(1L);
	     user.setMail(username);

	     Topic topic1 = new Topic();
	     topic1.setId(1L);
	     Topic topic2 = new Topic();
	     topic2.setId(2L);

	     Subscription subscription1 = new Subscription(user, topic1);
	     Subscription subscription2 = new Subscription(user, topic2);

	     List<Subscription> subscriptions = Stream.of(subscription1, subscription2)
	             .collect(Collectors.toList());
         Post post1 = new Post();
         post1.setTopic(topic1);
	     post1.setUser(user);
	     Post post2 = new Post();
	     post2.setTopic(topic2);
	     post2.setUser(user);

	     List<Post> posts = Stream.of(post1, post2).collect(Collectors.toList());

	     when(userRepository.findByMail(username)).thenReturn(Optional.of(user));
	     when(subscriptionRepository.findByUserId(user.getId())).thenReturn(subscriptions);
	     when(postRepository.findByTopicIdIn(anyList())).thenReturn(posts);

	     List<PostResponseDto> result = postService.getAll(username);

	     assertNotNull(result);
	     assertEquals(2, result.size());
	     verify(userRepository, times(1)).findByMail(username);
	     verify(subscriptionRepository, times(1)).findByUserId(user.getId());
	     verify(postRepository, times(1)).findByTopicIdIn(anyList());
	       
	}
	 
	 
	
	
	
	
	
	
	
	
}
