package backend.movie_service.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import backend.movie_service.model.Genre;
import java.util.Optional;

@Repository
public interface GenreRepository extends Neo4jRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
