package sm.dev.sv_trail.model.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String city;
    private String state;
    @Column(name = "tech_company", nullable = false)
    private String techCompany;
    @Column(name = "order_index", nullable = false)
    private int orderIndex;
    private double latitude;
    private double longitude;
}
