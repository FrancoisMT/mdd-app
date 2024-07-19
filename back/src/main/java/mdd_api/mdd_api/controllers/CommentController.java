package mdd_api.mdd_api.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import mdd_api.mdd_api.entities.Comment;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.mapper.CommentMapper;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.CommentService;
import mdd_api.mdd_api.services.PostService;
import mdd_api.mdd_api.services.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/comment")
public class CommentController {

	public CommentService commentService;
	public UserService userService;
	public PostService postService;
	
	public CommentController(CommentService commentService, UserService userService, PostService postService) {
		this.commentService = commentService;
		this.userService = userService;
		this.postService = postService;
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
        String mail = currentUser.getUsername();
        
        User user = userService.getUser(mail);
        Post post = postService.getById(postId);
        
        Comment comment = CommentMapper.toEntity(request, user, post);
       
        commentService.create(comment);
		
        return ResponseEntity.ok(new MessageResponseHandler("Comment successfully added"));		
	}
	
}
