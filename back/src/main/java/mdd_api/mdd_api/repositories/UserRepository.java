package mdd_api.mdd_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mdd_api.mdd_api.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByMail(String mail);
	boolean existsByMail(String mail);
	
}
