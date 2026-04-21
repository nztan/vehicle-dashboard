package ca.nztan.backend.service;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.entity.VehicleReading;
import ca.nztan.backend.repository.VehicleReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleReadingService {

    private final VehicleReadingRepository vehicleReadingRepository;

    public DashboardSnapshotDto getLatestDashboardSnapshot() {
        return toDto(vehicleReadingRepository.findTopByOrderByIdDesc());
    }

    public void save(VehicleReading vehicleReading) {
        vehicleReadingRepository.save(vehicleReading);
    }

    private DashboardSnapshotDto toDto(VehicleReading vehicleReading) {
        return new DashboardSnapshotDto()
                .setMotorRPM(vehicleReading.getMotorRpm())
                .setPowerKw(vehicleReading.getPowerKw())
                .setBatteryLevel(vehicleReading.getBatteryLevel())
                .setBatteryTemperature(vehicleReading.getBatteryTemperature())
                .setParkingBrake(vehicleReading.getParkingBrade())
                .setCheckEngine(vehicleReading.getCheckEngine());
    }
}
