package mdd_api.mdd_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.repositories.SubscriptionRepository;
import mdd_api.mdd_api.repositories.TopicRepository;
import mdd_api.mdd_api.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
	
	@Mock
	SubscriptionRepository subscriptionRepository;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	TopicRepository topicRepository;
	
	@InjectMocks
	SubscriptionService subscriptionService;
	 
	@Test
	public void testSubscribe_userNotFound_shouldThrowNotFoundException() {
		
	    Long topicId = 1L;
		String userMail = "test@test.fr";

	    when(userRepository.findByMail(userMail)).thenReturn(Optional.empty());
	    
	    CustomException exception = assertThrows(CustomException.class, () -> {
	        subscriptionService.subscribe(userMail, topicId);
	    });
	    
	    assertEquals("Error during user subscription: User not found with mail: " + userMail, exception.getMessage());
	    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
		
	}
	
	@Test
	public void testSubscribe_topicNotFound_shouldThrowNotFoundException() {
		
		Long userId = 1L;
	    Long topicId = 1L;
		String userMail = "test@test.fr";

	    User user = new User();
	    user.setId(userId);
	    
	    when(userRepository.findByMail(userMail)).thenReturn(Optional.of(user));
	    when(topicRepository.findById(topicId)).thenReturn(Optional.empty());
	    
	    CustomException exception = assertThrows(CustomException.class, () -> {
	        subscriptionService.subscribe(userMail, topicId);
	    });
	    
	    assertEquals("Error during user subscription: Topic not found with id: " + topicId, exception.getMessage());
	    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
		
	}
	
	@Test
	public void testSubscribe_userAlreadySubscribed_shouldThrowConflictException() {
		
		Long userId = 1L;
	    Long topicId = 1L;
		String userMail = "test@test.fr";

	    User user = new User();
	    user.setId(userId);
	    
	    Topic topic = new Topic();
	    topic.setId(topicId);
	    
	    when(userRepository.findByMail(userMail)).thenReturn(Optional.of(user));
	    when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
	    when(subscriptionRepository.existsByUserIdAndTopicId(userId, topicId)).thenReturn(true);
	    
	    CustomException exception = assertThrows(CustomException.class, () -> {
	        subscriptionService.subscribe(userMail, topicId);
	    });
	    
	    assertEquals("User is already subscribed to this topic", exception.getMessage());
	    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
		
	}
	
	@Test
	public void testSubscribe_success() {
		
		Long userId = 1L;
		String userMail = "test@test.fr";
	    Long topicId = 1L;
	    
	    User user = new User();
	    user.setId(userId);
	    user.setMail("test@test.fr");
	    
	    Topic topic = new Topic();
	    topic.setId(topicId);
	    
	    when(userRepository.findByMail(userMail)).thenReturn(Optional.of(user));
	    when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
	    when(subscriptionRepository.existsByUserIdAndTopicId(userId, topicId)).thenReturn(false);
	    
	    subscriptionService.subscribe(userMail, topicId);
	    
	    verify(subscriptionRepository, times(1)).save(any(Subscription.class));
		
	}
	
	@Test
	public void testUnsubscribe_subscriptionNotFound_shouldThrowNotFoundException() {
		
		String userMail = "test@test.fr";
		Long subscriptionId = 1L;
		
	    when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

	    CustomException exception = assertThrows(CustomException.class, () -> {
            subscriptionService.unsubscribe(subscriptionId, userMail);
        });
		
	    assertEquals("Error during user unsubscription: Subscription not found with id: " + subscriptionId, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

	}
	
	@Test
	public void testUnsubscribe_userNotAuthorized_shouldThrowAccessDeniedException() {
		
		 Long userId = 1L;
	     String userMail = "test@test.fr";
	     Long subscriptionId = 1L;
	     
	     User unauthorizedUser = new User();
	     unauthorizedUser.setId(999L); 

	     User user = new User();
	     user.setId(userId);
	     user.setMail(userMail); 

	     Subscription subscription = new Subscription();
	     subscription.setId(subscriptionId);
	     subscription.setUser(unauthorizedUser);

	     when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
	     when(userRepository.findByMail(userMail)).thenReturn(Optional.of(user));

	      CustomException exception = assertThrows(CustomException.class, () -> {
	            subscriptionService.unsubscribe(subscriptionId, userMail);
	      });

	     assertEquals("Access denied: You are not authorized to unsubscribe from this subscription", exception.getMessage());
	     assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

	}
	
	@Test
	public void testUnsubscribe_success() {
	
		Long userId = 1L;
		String userMail = "test@test.fr";
        Long subscriptionId = 1L;
        
        User user = new User();
        user.setId(userId);

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(userRepository.findByMail(userMail)).thenReturn(Optional.of(user));

        subscriptionService.unsubscribe(subscriptionId, userMail);

        verify(subscriptionRepository, times(1)).deleteById(subscriptionId);
		
	}
	
	
	
	

}
