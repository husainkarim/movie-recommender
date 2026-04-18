package backend.rating_service.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.rating_service.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
       @Query("MATCH (u:User) WHERE elementId(u) = $userId " +
              "MATCH (m:Movie) WHERE elementId(m) = $movieId " +
              "MERGE (u)-[r:RATED]->(m) " +
              "SET r.rating = $rating, r.timestamp = timestamp() " +
              "RETURN r")
       void addRating(String userId, String movieId, Double rating);

       @Query("MATCH (u:User) WHERE elementId(u) = $userId " +
              "MATCH (m:Movie) WHERE elementId(m) = $movieId " +
              "MATCH (u)-[r:RATED]->(m) " +
              "DELETE r")
       void deleteRating(String userId, String movieId);
}
