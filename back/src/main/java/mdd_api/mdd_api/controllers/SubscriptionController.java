package mdd_api.mdd_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.SubscriptionService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

	public SubscriptionService subscriptionService;
	
	public SubscriptionController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	@PostMapping("/subscribe/{topicId}")
	public ResponseEntity<MessageResponseHandler> subscribe(@PathVariable Long topicId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String mail = currentUser.getUsername();
                
        subscriptionService.subscribe(mail, topicId);
        
        return ResponseEntity.ok(new MessageResponseHandler("Ok"));		
	}
	
	@DeleteMapping("/unsubscribe/{subId}")
	public ResponseEntity<MessageResponseHandler> unsubscribe(@PathVariable Long subId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String mail = currentUser.getUsername();
		
        subscriptionService.unsubscribe(subId, mail);

        return ResponseEntity.ok(new MessageResponseHandler("Ok"));		
		
	}
	
	 @GetMapping("/user/all")
	 public ResponseEntity<List<Subscription>> getAllSubscriptionsByUserId() {
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     User currentUser = (User) authentication.getPrincipal();
	     Long userId = currentUser.getId();
	     
	     List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsByUserId(userId);
	     return ResponseEntity.ok(subscriptions);
	     
	 }
	
	
	
}
