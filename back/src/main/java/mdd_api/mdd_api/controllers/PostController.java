package mdd_api.mdd_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
	
	public PostService service;
	
	public PostController(PostService service) {
		this.service = service;
	}

	@PostMapping("/create")
	@Operation(summary = "Post a new post", description = "This operation inserts a new Post in DB.")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Post successfullly created!", 
	    		 content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
	     @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<MessageResponseHandler> create(@Valid @RequestBody PostDto request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String mail = currentUser.getUsername();
              
        service.create(mail, request);
		
        return ResponseEntity.ok(new MessageResponseHandler("Post successfully created"));		

	}
	
	@GetMapping("/all")
	@Operation(summary = "Get all user's post (only if it is associated with a subscribed topic)")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "List successfully retrieved", 
	    		 content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
	     @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<List<PostResponseDto>> getAll() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String username = currentUser.getUsername();
        
        List<PostResponseDto> list = service.getAll(username);
        
        return ResponseEntity.ok().body(list);
		
	}
	
	
	@GetMapping("/{id}")
	@Operation(summary = "Get a post by id")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Post successfully retrieved", 
	    		 content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
	     @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<PostResponseDto> getById(@PathVariable Long id) {
				
		PostResponseDto post = service.getById(id);
		
		return ResponseEntity.ok().body(post);
		
	}
	
	
	
}
