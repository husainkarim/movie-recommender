package backend.recommendation_service.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.recommendation_service.model.Movie;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
    // Collaborative Filtering ("Users who liked this also liked...")
    // This logic finds users who have rated the same movies as you
    @Query("MATCH (u:User {userId: $userId})-[r1:RATED]->(m:Movie)<-[r2:RATED]-(other:User) " +
        "MATCH (other)-[r3:RATED]->(rec:Movie) " +
        "WHERE NOT (u)-[:RATED]->(rec) " +
        "AND r1.rating >= 4 AND r2.rating >= 4 AND r3.rating >= 4 " +
        "RETURN rec, COUNT(*) AS score " +
        "ORDER BY score DESC LIMIT 5")
    List<Movie> getCollaborativeRecommendations(String userId);

    // Content-Based ("Because you liked Inception...")
    // This looks at the attributes of movies you rated highly 
    // (like Genres or Directors) and finds other movies with those same attributes.
    @Query("MATCH (u:User {userId: $userId})-[r:RATED]->(m:Movie)-[:IN_GENRE|DIRECTED]-(attribute) " +
        "MATCH (rec:Movie)-[:IN_GENRE|DIRECTED]-(attribute) " +
        "WHERE r.rating >= 4 AND NOT (u)-[:RATED]->(rec) AND m <> rec " +
        "RETURN rec, COUNT(*) AS score " +
        "ORDER BY score DESC LIMIT 5")
    List<Movie> getContentBasedRecommendations(String userId);

    // Similarity ("Similar to this movie")
    // This is useful for the GET /similar/{movieId} endpoint.
    // It finds movies that are frequently rated highly by the same group of users.
    @Query("MATCH (m:Movie {id: $movieId})<-[:RATED]-(u:User)-[:RATED]->(rec:Movie) " +
        "WHERE m <> rec " +
        "RETURN rec, COUNT(*) AS commonUsers " +
        "ORDER BY commonUsers DESC LIMIT 5")
    List<Movie> getSimilarMovies(Long movieId);

    // get top 5 movies by average rating
    @Query("MATCH (m:Movie)<-[r:RATED]-() " +
        "WITH m, AVG(r.rating) AS avgRating " +
        "ORDER BY avgRating DESC " +
        "RETURN m LIMIT 5")
    List<Movie> getTopRatedMovies();
}
