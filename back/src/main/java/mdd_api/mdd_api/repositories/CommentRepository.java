package mdd_api.mdd_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mdd_api.mdd_api.entities.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long>{

	public List<Comment> findByPostId(Long postId);
	
}
