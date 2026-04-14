package sm.dev.sv_trail.model.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import sm.dev.sv_trail.model.enums.GameState;
import sm.dev.sv_trail.model.enums.LossReason;
import sm.dev.sv_trail.model.enums.PlayerRole;

@Entity
@Table(name = "game_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSession {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_location_id", nullable = false)
    private Location currentLocation;
    @Enumerated(EnumType.STRING)
    private PlayerRole role;
    @Enumerated(EnumType.STRING)
    private GameState state;
    @Enumerated(EnumType.STRING)
    @Column(name = "loss_reason")
    private LossReason lossReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pending_event_id")
    private Event pendingEvent;
    @Column(name = "final_score")
    private int finalScore;
    
    @Column(name = "day_number", nullable = false)
    private int dayNumber;
    private int cash;
    private int morale;
    private int hype;
    @Column(name = "bug_count", nullable = false)
    private int bugCount;
    @Column(name = "coffee_supply", nullable = false)
    private int coffeeSupply;
    @Setter(AccessLevel.NONE)
    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        if (startedAt == null) startedAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
