package backend.movie_service.model;

import org.springframework.data.neo4j.core.schema.Node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("Person")
public class Person {
    private String name;
    private Integer born;
}
