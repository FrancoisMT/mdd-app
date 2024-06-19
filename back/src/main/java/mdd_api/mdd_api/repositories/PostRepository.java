package mdd_api.mdd_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mdd_api.mdd_api.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

	List<Post> findByTopicIdIn(List<Long> topicIds);

}
