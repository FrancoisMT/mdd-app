package mdd_api.mdd_api.services;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.repositories.SubscriptionRepository;
import mdd_api.mdd_api.repositories.TopicRepository;
import mdd_api.mdd_api.repositories.UserRepository;

@Service
public class SubscriptionService {

	public SubscriptionRepository subscriptionRepository;
	public UserRepository userRepository;
	public TopicRepository topicRepository;
	
	public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, TopicRepository topicRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.userRepository = userRepository;
		this.topicRepository = topicRepository;
	}
	
	public void subscribe(String mail, Long topicId) {
			
            User user = userRepository.findByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + mail));
            
            Long userId = user.getId();
            
            Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + topicId));
            
            boolean alreadySubscribed = subscriptionRepository.existsByUserIdAndTopicId(userId, topicId);
            
            if (alreadySubscribed) {
                throw new CustomException("User is already subscribed to this topic", HttpStatus.CONFLICT);
            }
            
            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setTopic(topic);
            
            subscriptionRepository.save(subscription);
    }
	
	public void unsubscribe(Long id, String mail) {

	        Subscription subscription = subscriptionRepository.findById(id)
	        	  .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + id));
	        
	        User user = userRepository.findByMail(mail)
	        	  .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + mail));
	        
	        Long userId = user.getId();
	        
	        if (!subscription.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("You are not authorized to unsubscribe from this subscription");
            }
	        
	        subscriptionRepository.deleteById(id);
	}
	
	 public List<Subscription> getAllSubscriptionsByUserId(Long userId) {
		 List<Subscription> list = subscriptionRepository.findByUserId(userId);
	     return list;
	     
	 }
	
	
}
