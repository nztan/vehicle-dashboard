package ca.nztan.backend.service;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.dto.GearType;
import ca.nztan.backend.entity.VehicleReading;
import ca.nztan.backend.repository.VehicleReadingRepository;
import ca.nztan.backend.simulator.VehicleState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleReadingService {

    private final static int VEHICLE_READING_ID = 1;
    private final static int CHECK_ENGINE_THRESHOLD = 800;

    private final VehicleReadingRepository vehicleReadingRepository;

    public DashboardSnapshotDto getDashboardSnapshot() {
        return vehicleReadingRepository.findById(VEHICLE_READING_ID)
                .map(this::toDto)
                .orElseGet(this::getDefaultDashboardSnapshot);
    }

    public void save(VehicleState vehicleState) {
        vehicleReadingRepository.save(toEntity(vehicleState));
    }

    private DashboardSnapshotDto getDefaultDashboardSnapshot() {
        return new DashboardSnapshotDto()
                .setMotorRPM(0)
                .setPowerKw(0)
                .setBatteryLevel(0)
                .setBatteryTemperature(0)
                .setParkingBrake(false)
                .setCheckEngine(false);
    }

    private VehicleReading toEntity(VehicleState vehicleState) {
        return VehicleReading.builder()
                .id(VEHICLE_READING_ID)
                .motorRpm(vehicleState.getMotorRpm())
                .powerKw(vehicleState.getPowerKw())
                .batteryLevel(vehicleState.getBatteryLevel())
                .batteryTemperature(vehicleState.getBatteryTemperature())
                .parkingBrade(GearType.P.equals(vehicleState.getGear()))
                .checkEngine(vehicleState.getMotorRpm() >= CHECK_ENGINE_THRESHOLD)
                .build();
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
