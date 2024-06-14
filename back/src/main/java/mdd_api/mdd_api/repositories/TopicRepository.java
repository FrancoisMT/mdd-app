package mdd_api.mdd_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mdd_api.mdd_api.entities.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{
	
}
