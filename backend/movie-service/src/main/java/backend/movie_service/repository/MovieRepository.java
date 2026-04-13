package backend.movie_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import backend.movie_service.model.Movie;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    // get all years of movies in the database
    @Query("MATCH (m:Movie) RETURN DISTINCT m.released AS year ORDER BY year DESC")
    List<Integer> findAllYears();
}
