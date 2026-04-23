package ca.nztan.backend.repository;

import ca.nztan.backend.entity.VehicleReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleReadingRepository extends JpaRepository<VehicleReading, UUID> {
}
