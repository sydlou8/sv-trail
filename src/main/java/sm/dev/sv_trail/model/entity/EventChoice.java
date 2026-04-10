package sm.dev.sv_trail.model.entity;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "event_choices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventChoice {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    private String description;
    @Column(name = "cash_delta")
    private int cashDelta;
    @Column(name = "morale_delta")
    private int moraleDelta;
    @Column(name = "hype_delta")
    private int hypeDelta;
    @Column(name = "bug_count_delta")
    private int bugCountDelta;
    @Column(name = "coffee_supply_delta")
    private int coffeeSupplyDelta;
}
