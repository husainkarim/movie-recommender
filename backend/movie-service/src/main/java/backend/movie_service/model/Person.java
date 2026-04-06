package backend.movie_service.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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
@Node("Person")
public class Person {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private Integer born;

    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.OUTGOING)
    @Getter(AccessLevel.NONE)
    private List<Movie> moviesActedIn;
    // get the movie titles & ids for the movies this person acted + the role they played in each movie
    public List<Map<String, Object>> getMoviesActedIn() {
        if (moviesActedIn == null) return List.of();
        return moviesActedIn.stream()
                .map(movie -> {
                    Map<String, Object> movieMap = new HashMap<>();
                    movieMap.put("id", movie.getId());
                    movieMap.put("title", movie.getTitle());
                    String role = movie.getActors().stream()
                            .filter(actor -> this.name != null && this.name.equals(actor.get("name")))
                            .findFirst()
                            .map(actor -> (String) actor.get("role"))
                            .orElse("Unknown Role");
                    movieMap.put("role", role);
                    return movieMap;
                })
                .collect(Collectors.toList());
    }
}
