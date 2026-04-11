package backend.movie_service.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
    
    @Relationship(type = "HAS_GENRE", direction = Relationship.Direction.OUTGOING)
    private Set<Genre> genres = new HashSet<>();

    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
    @Getter(AccessLevel.NONE)
    private Set<ActedIn> actedInRelationships = new HashSet<>();
    
    @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
    private Set<Person> directors = new HashSet<>();

    @Relationship(type = "RATED", direction = Relationship.Direction.INCOMING)
    private Set<Rated> ratings = new HashSet<>();

    public List<Map<String, Object>> getActors() {
        if (actedInRelationships == null) return Collections.emptyList();
        return actedInRelationships.stream()
                .map(actedIn -> {
                    Map<String, Object> actorMap = new HashMap<>();
                    actorMap.put("name", actedIn.getPerson().getName());
                    actorMap.put("born", actedIn.getPerson().getBorn());
                    actorMap.put("role", actedIn.getRoles().get(0)); // Assuming a single role for simplicity
                    return actorMap;
                })
                .collect(Collectors.toList());
    }
}
