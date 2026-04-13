package backend.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.user_service.model.Movie;
import backend.user_service.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ADD to watchlist
    @Query("MATCH (u:User {id: $userId}), (m:Movie {id: $movieId}) " +
        "MERGE (u)-[:WATCHLIST]->(m)")
    void addToWatchlist(Long userId, Long movieId);

    // DELETE from watchlist
    @Query("MATCH (u:User {id: $userId})-[r:WATCHLIST]->(m:Movie {id: $movieId}) " +
        "DELETE r")
    void removeFromWatchlist(Long userId, Long movieId);

    // LIST movies in watchlist
    @Query("MATCH (u:User {id: $userId})-[:WATCHLIST]->(m:Movie) " +
        "RETURN m")
    List<Movie> findWatchlistByUserId(Long userId);
}
