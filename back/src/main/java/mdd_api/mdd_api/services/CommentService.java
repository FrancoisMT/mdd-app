package mdd_api.mdd_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.entities.Comment;
import mdd_api.mdd_api.repositories.CommentRepository;
import mdd_api.mdd_api.repositories.PostRepository;
import mdd_api.mdd_api.repositories.UserRepository;

@Service
public class CommentService {
	
	public CommentRepository commentRepository;
	public UserRepository userRepository;
	public PostRepository postRepository;
	
	public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}
	
	public void create(Comment comment) {

		commentRepository.save(comment);	
	}
	
	public List<Comment> getAll(Long postId) {
			
		postRepository.findById(postId)
					.orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
			
		List<Comment> comments = commentRepository.findByPostId(postId);
		
		return comments;
	}
	
}
