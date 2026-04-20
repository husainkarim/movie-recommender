package backend.user_service.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private String id;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    @Size(min=8, max=100, message="Password must be between 8 and 100 characters")
    @JsonIgnore
    private String password;
    @NotBlank
    private String role;
    
    @JsonIgnore
    private String twoFactorSecret;
    private boolean twoFactorEnabled;

    @Relationship(type = "RATED", direction = Relationship.Direction.OUTGOING)
    private List<Rated> ratings = new ArrayList<>();

    @Relationship(type = "WATCHLIST", direction = Relationship.Direction.OUTGOING)
    private Set<WatchList> watchlist = new HashSet<>();
}
