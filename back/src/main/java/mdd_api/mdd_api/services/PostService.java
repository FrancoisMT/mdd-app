package mdd_api.mdd_api.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.Subscription;
import mdd_api.mdd_api.entities.User;
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
	
	public void create(Post post) {
					 
		postRepository.save(post);
	}
	
	public List<Post> getAll(String username) {
			
		User currentUser = userRepository.findByMail(username)
	                .orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + username));
			
		Long userId = currentUser.getId();
			
		List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);

	    List<Long> topicIds = subscriptions.stream()
	                 .map(subscription -> subscription.getTopic().getId())
	                 .collect(Collectors.toList());

	    List<Post> posts = postRepository.findByTopicIdIn(topicIds);
	         
	    return posts;		
	}
	
	public Post getById(Long id) {
			
		Post post = postRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
			
		return post;	
	}
	
	
}
