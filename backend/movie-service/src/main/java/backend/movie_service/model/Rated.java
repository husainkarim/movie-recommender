package backend.movie_service.model;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Rated {
    private Long id;
    private Double rating; // e.g., 1.0 to 5.0

    @TargetNode
    private User user;
}