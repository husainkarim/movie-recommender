package backend.user_service.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.user_service.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    Optional<User> findByEmail(String email);

    // ADD to watchlist
    @Query("MATCH (u:User) WHERE elementId(u) = $userId " +
        "MATCH (m:Movie) WHERE elementId(m) = $movieId " +
        "MERGE (u)-[:WATCHLIST]->(m)")
    void addToWatchlist(String userId, String movieId);

    // DELETE from watchlist
    @Query("MATCH (u:User) WHERE elementId(u) = $userId " +
        "MATCH (m:Movie) WHERE elementId(m) = $movieId " +
        "MATCH (u)-[r:WATCHLIST]->(m) " +
        "DELETE r")
    void removeFromWatchlist(String userId, String movieId);
}
