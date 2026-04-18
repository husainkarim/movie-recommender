package backend.movie_service.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import backend.movie_service.model.Person;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, String> {
    Optional<Person> findByName(String name);
}
