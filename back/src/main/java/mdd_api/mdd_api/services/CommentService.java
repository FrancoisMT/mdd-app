package mdd_api.mdd_api.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mdd_api.mdd_api.dto.CommentRequestDto;
import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.entities.Comment;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.User;
import mdd_api.mdd_api.exceptions.CustomException;
import mdd_api.mdd_api.mapper.CommentMapper;
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
	
	public void create(String username, CommentRequestDto commentDto, Long postId) {
		
		try {
			
			User currentUser = userRepository.findByMail(username)
					.orElseThrow(() -> new EntityNotFoundException("User not found with mail: " + username));
			
			Post post = postRepository.findById(postId)
					.orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
			
			Comment comment = new Comment();
			comment.setContent(commentDto.getContent());
			comment.setUser(currentUser);
			comment.setPost(post);
			
			commentRepository.save(comment);
			
		} catch (EntityNotFoundException e) {
	        throw new CustomException("Error creating post: " + e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        throw new CustomException("Error creating post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
				
	}
	
	public List<CommentResponseDto> getAll(Long postId) {
		
		try {
			
			postRepository.findById(postId)
					.orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
			
			
			List<Comment> comments = commentRepository.findByPostId(postId);
			
			return CommentMapper.toDtoList(comments);		
			
		} catch (EntityNotFoundException e) {
	        throw new CustomException("Error creating post: " + e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        throw new CustomException("Error creating post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
		
	}
	

}
