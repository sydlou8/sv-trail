package sm.dev.sv_trail.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sm.dev.sv_trail.model.entity.EventChoice;

@Repository
public interface EventChoiceRepository extends JpaRepository<EventChoice, UUID> {
    Optional<List<EventChoice>> findByEventId(UUID eventId);
}
