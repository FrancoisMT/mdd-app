package mdd_api.mdd_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mdd_api.mdd_api.entities.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	List<Subscription> findByUserId(Long userId);
	boolean existsByUserIdAndTopicId(Long userId, Long topicId);
	
}
