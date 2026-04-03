package backend.movie_service.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import backend.movie_service.model.Movie;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    
}
