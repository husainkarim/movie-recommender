package backend.movie_service.model;

import java.util.List;

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
public class ActedIn {
    @RelationshipId
    private Long id;

    private List<String> roles;

    @TargetNode
    private Person person;
}
