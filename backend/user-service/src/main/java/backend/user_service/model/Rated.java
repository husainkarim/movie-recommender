package backend.user_service.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RelationshipProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rated {

    @RelationshipId
    private Long id;

    private Double rating; // e.g., 1.0 to 5.0
    private Long timestamp; // Useful for "Recent Ratings" logic

    @TargetNode
    private Movie movie;
}
