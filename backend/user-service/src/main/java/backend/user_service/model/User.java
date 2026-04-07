package backend.user_service.model;

import java.util.ArrayList;
import java.util.List;

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
@Node("User")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String role;

    @Relationship(type = "RATED", direction = Relationship.Direction.OUTGOING)
    private List<Rated> ratings = new ArrayList<>();

}
