package mdd_api.mdd_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.SubscriptionService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/subscription")
public class SubscriptionController {

	public SubscriptionService subscriptionService;
	
	public SubscriptionController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	@PostMapping("/subscribe/{topicId}")
	@Operation(summary = "Subscribe to a specific topic")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Successfull subscription", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = MessageResponseHandler.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
		     @ApiResponse(responseCode = "409", description = "User is already subscribed to this topic", content = @Content)
	})
	public ResponseEntity<MessageResponseHandler> subscribe(@PathVariable Long topicId) {
		System.out.println("okkkkkkkkkkkkkkkkk");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String mail = currentUser.getUsername();
                
        subscriptionService.subscribe(mail, topicId);
        
        return ResponseEntity.ok(new MessageResponseHandler("Ok"));		
	}
	
	@DeleteMapping("/unsubscribe/{subId}")
	@Operation(summary = "unsubscribe to a specific topic")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Successfull unsubscription", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = MessageResponseHandler.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
		     @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
	})
	public ResponseEntity<MessageResponseHandler> unsubscribe(@PathVariable Long subId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String mail = currentUser.getUsername();
		
        subscriptionService.unsubscribe(subId, mail);

        return ResponseEntity.ok(new MessageResponseHandler("Ok"));		
		
	}
	
	 @GetMapping("/user/all")
	 @Operation(summary = "retrieve all subscription for one user")
		@ApiResponses(value = {
			     @ApiResponse(responseCode = "200", description = "Data successfully retrieved", 
			    		 content = @Content(mediaType = "application/json",
		                 schema = @Schema(implementation = MessageResponseHandler.class))),
			     @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
	 })
	 public ResponseEntity<List<Subscription>> getAllSubscriptionsByUserId() {
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     User currentUser = (User) authentication.getPrincipal();
	     Long userId = currentUser.getId();
	     
	     List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsByUserId(userId);
	     return ResponseEntity.ok(subscriptions);
	     
	 }
	
	
	
}
