package backend.movie_service.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("Movie")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Integer released;
    private String tagline;
    
    // Direction is OUTGOING: (Movie)-[:HAS_GENRE]->(Genre)
    @Relationship(type = "HAS_GENRE", direction = Relationship.Direction.OUTGOING)
    private Set<Genre> genres = new HashSet<>();

    // Direction is INCOMING: (Person)-[:ACTED_IN]->(Movie)
    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
    private Set<Person> actors = new HashSet<>();
    
    @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
    private Set<Person> directors = new HashSet<>();
}
