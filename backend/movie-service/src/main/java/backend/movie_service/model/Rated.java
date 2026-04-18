package backend.movie_service.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Rated {
    @RelationshipId
    private String id;
    private Double rating; // e.g., 1.0 to 5.0
    private Long timestamp; // Unix timestamp of when the rating was made

    @TargetNode
    private User user;
}