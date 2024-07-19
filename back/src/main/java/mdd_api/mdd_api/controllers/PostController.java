package mdd_api.mdd_api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.Comment;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.mapper.CommentMapper;
import mdd_api.mdd_api.mapper.PostMapper;
import mdd_api.mdd_api.mapper.UserMapper;
import mdd_api.mdd_api.payload.response.MessageResponseHandler;
import mdd_api.mdd_api.services.CommentService;
import mdd_api.mdd_api.services.PostService;
import mdd_api.mdd_api.services.TopicService;
import mdd_api.mdd_api.services.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/posts")
public class PostController {
	
	public UserService userService;
	public PostService postService;
	public CommentService commentService;
	public TopicService topicService;
	
	public PostController(PostService postService, CommentService commentService, TopicService topicService, UserService userService) {
		this.postService = postService;
		this.commentService = commentService;
		this.topicService = topicService;
		this.userService = userService;
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
        
        User user = userService.getUser(mail);
        
        Long topicId = request.getTopicId();
        Topic topic = topicService.getById(topicId);
        Post post = PostMapper.toEntity(request, user, topic);
              
        postService.create(post);
		
        return ResponseEntity.ok(new MessageResponseHandler("Article créé avec succès !"));		
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
        
        List<Post> posts = postService.getAll(username);
        
        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(post -> {
                    Topic topic = post.getTopic();
                    User user = post.getUser();
                    UserDto userDto = UserMapper.toDto(user);
                    return PostMapper.toResponseDto(post, topic, userDto, null);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok().body(postResponseDtos);
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
				
		Post post = postService.getById(id);
		Topic topic = post.getTopic();
		User user = post.getUser();
		UserDto userDto = UserMapper.toDto(user);
		
		List<Comment> comments = commentService.getAll(id);
		List<CommentResponseDto> commentsDtoList = CommentMapper.toDtoList(comments);
		
		PostResponseDto postResponseDto = PostMapper.toResponseDto(post, topic, userDto, commentsDtoList);
		
		return ResponseEntity.ok().body(postResponseDto);
	}
	

	
}
