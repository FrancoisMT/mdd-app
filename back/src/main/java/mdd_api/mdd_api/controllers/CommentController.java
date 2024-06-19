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
	public ResponseEntity<MessageResponseHandler> create(@Valid @RequestBody CommentRequestDto request, @PathVariable Long postId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String username = currentUser.getUsername();
        
        commentService.create(username, request, postId);
		
        return ResponseEntity.ok(new MessageResponseHandler("Comment successfully added"));		

	}
	
	@GetMapping("/{postId}/all")
	public ResponseEntity<List<CommentResponseDto>> getAll(@PathVariable Long postId) {
		
		List<CommentResponseDto> list = commentService.getAll(postId);
		
		return ResponseEntity.ok().body(list);	
	}
	
}
