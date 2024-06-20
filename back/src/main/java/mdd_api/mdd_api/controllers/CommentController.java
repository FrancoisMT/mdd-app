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
import mdd_api.mdd_api.dto.CommentRequestDto;
import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {

	public CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping("/{postId}/create")
	@Operation(summary = "Post a new comment", description = "This operation inserts a new comment in DB.")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Comment successfullly created!", 
	    		 content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
	     @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<MessageResponseHandler> create(@Valid @RequestBody CommentRequestDto request, @PathVariable Long postId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String username = currentUser.getUsername();
        
        commentService.create(username, request, postId);
		
        return ResponseEntity.ok(new MessageResponseHandler("Comment successfully added"));		

	}
	
	@GetMapping("/{postId}/all")
	@PostMapping("/{postId}/create")
	@Operation(summary = "Get one post' comments", description = "This operation retrieved all comments associated wuith a specific post.")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "List successfully retrieved", 
	    		 content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = MessageResponseHandler.class))),
	     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	     @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
	     @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<List<CommentResponseDto>> getAll(@PathVariable Long postId) {
		
		List<CommentResponseDto> list = commentService.getAll(postId);
		
		return ResponseEntity.ok().body(list);	
	}
	
}
