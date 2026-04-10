package sm.dev.sv_trail.model.entity;

import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // "user" is a reserved keyword in postgresql
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails{
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name= "password_hash", nullable = false)
    private String password;
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    public List<GrantedAuthority> getAuthorities() {
        return List.of();
    }

}