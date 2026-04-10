package sm.dev.sv_trail.model.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import sm.dev.sv_trail.model.enums.EventSource;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Event {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = true)
    private Location location;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_source", nullable = false)
    private EventSource eventSource;
    private int weight;
    private boolean active;
}
