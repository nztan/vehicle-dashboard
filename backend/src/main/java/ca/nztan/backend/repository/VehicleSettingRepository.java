package ca.nztan.backend.repository;

import ca.nztan.backend.entity.VehicleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleSettingRepository extends JpaRepository<VehicleSetting, Integer> {
}
