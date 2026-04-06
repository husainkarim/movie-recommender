package backend.user_service.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import backend.user_service.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
