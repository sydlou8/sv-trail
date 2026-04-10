package sm.dev.sv_trail.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sm.dev.sv_trail.model.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByOrderIndex(int orderIndex);
    Optional<Location> findFirstByOrderIndexGreaterThan(int orderIndex);
    Optional<Location> findTopByOrderByOrderIndexDesc();
}
