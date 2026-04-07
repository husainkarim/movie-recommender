package backend.rating_service.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.rating_service.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
       @Query("MATCH (u:User {userId: $userId}), (m:Movie {id: $movieId}) " +
              "MERGE (u)-[r:RATED]->(m) " +
              "SET r.rating = $rating, r.timestamp = timestamp() " +
              "RETURN r")
       void addRating(Long userId, Long movieId, Double rating);

       @Query("MATCH (u:User {userId: $userId})-[r:RATED]->(m:Movie {id: $movieId}) " +
              "DELETE r")
       void deleteRating(Long userId, Long movieId);
}
