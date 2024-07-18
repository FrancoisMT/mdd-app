package mdd_api.mdd_api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.mapper.PostMapper;
import mdd_api.mdd_api.mapper.UserMapper;
import mdd_api.mdd_api.repositories.PostRepository;
import mdd_api.mdd_api.repositories.SubscriptionRepository;
import mdd_api.mdd_api.repositories.TopicRepository;
import mdd_api.mdd_api.repositories.UserRepository;

@Service
public class PostService {

	public PostRepository postRepository;
	public UserRepository userRepository;
	public TopicRepository topicRepository;
	public SubscriptionRepository subscriptionRepository;
	public CommentService commentService;
	
	public PostService(PostRepository postRepository, UserRepository userRepository, 
			TopicRepository topicRepository,SubscriptionRepository subscriptionRepository, CommentService commentService) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.topicRepository = topicRepository;
		this.subscriptionRepository = subscriptionRepository;
		this.commentService = commentService;
	}
	
	public void create(String mail, PostDto postDto) {
		
		User user = userRepository.findByMail(mail)
		                .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + mail));
			 
		Topic topic = topicRepository.findById(postDto.getTopicId())
					 	.orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + postDto.getTopicId()));
			 
		Post post = PostMapper.toEntity(postDto, user, topic);
			 
		postRepository.save(post);
	}
	
	public List<PostResponseDto> getAll(String username) {
			
		User currentUser = userRepository.findByMail(username)
	                .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + username));
			
		Long userId = currentUser.getId();
			
		List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);

	    List<Long> topicIds = subscriptions.stream()
	                 .map(subscription -> subscription.getTopic().getId())
	                 .collect(Collectors.toList());

	    List<Post> posts = postRepository.findByTopicIdIn(topicIds);
	         
	    List<PostResponseDto> postResponseDtos = new ArrayList<>();
	         
	    for (Post post : posts) {
	                Topic topic = post.getTopic();
	                User user = post.getUser();
	                UserDto userDto = UserMapper.toDto(user);
	                
	                PostResponseDto postResponseDto = PostMapper.toResponseDto(post, topic, userDto, null);
	                postResponseDtos.add(postResponseDto);
	    }

	    return postResponseDtos;		
	}
	
	public PostResponseDto getById(Long id) {
			
		Post post = postRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
		Topic topic = post.getTopic();
		User user = post.getUser();
		UserDto userDto = UserMapper.toDto(user);
			
		List<CommentResponseDto> comments = commentService.getAll(id);
			
		PostResponseDto postResponseDto = PostMapper.toResponseDto(post, topic, userDto, comments);
			
		return postResponseDto;	
	}
	
	
	
}
