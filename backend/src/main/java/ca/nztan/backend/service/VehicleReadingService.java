package ca.nztan.backend.service;

import ca.nztan.backend.dto.VehicleReadingDto;
import ca.nztan.backend.entity.VehicleReading;
import ca.nztan.backend.repository.VehicleReadingRepository;
import ca.nztan.backend.simulator.VehicleState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleReadingService {

    private final static int CHECK_ENGINE_THRESHOLD = 800;
    private final static int BATTERY_LOW_THRESHOLD = 20;
    private final static int MOTOR_STATUS_THRESHOLD = 500;
    private final static String GEAR_RATIO_PARK = "N/N";

    private final VehicleReadingRepository vehicleReadingRepository;

    public VehicleReadingDto get(UUID vehicleId) {
        return vehicleReadingRepository.findById(vehicleId)
                .map(this::toDto)
                .orElseGet(this::getDefault);
    }

    public void save(UUID vehicleId, VehicleState vehicleState) {
        vehicleReadingRepository.save(toEntity(vehicleId, vehicleState));
    }

    private VehicleReadingDto getDefault() {
        return new VehicleReadingDto()
                .setMotorRPM(0)
                .setPowerKw(0)
                .setBatteryLevel(0)
                .setBatteryTemperature(0)
                .setGearRatio(GEAR_RATIO_PARK)
                .setParkingBrake(false)
                .setCheckEngine(false)
                .setBatteryLow(false)
                .setMotorStatusWarning(false);
    }

    private VehicleReading toEntity(UUID vehicleId, VehicleState vehicleState) {
        return VehicleReading.builder()
                .vehicleId(vehicleId)
                .motorRpm(vehicleState.getMotorRpm())
                .powerKw(vehicleState.getPowerKw())
                .batteryLevel(vehicleState.getBatteryLevel())
                .batteryTemperature(vehicleState.getBatteryTemperature())
                .gearRatio(vehicleState.getGearRatio())
                .parkingBrake(GEAR_RATIO_PARK.equals(vehicleState.getGearRatio()))
                .checkEngine(vehicleState.getMotorRpm() >= CHECK_ENGINE_THRESHOLD)
                .build();
    }

    private VehicleReadingDto toDto(VehicleReading vehicleReading) {
        return new VehicleReadingDto()
                .setMotorRPM(vehicleReading.getMotorRpm())
                .setPowerKw(vehicleReading.getPowerKw())
                .setBatteryLevel(vehicleReading.getBatteryLevel())
                .setBatteryTemperature(vehicleReading.getBatteryTemperature())
                .setGearRatio(vehicleReading.getGearRatio())
                .setParkingBrake(vehicleReading.getParkingBrake())
                .setCheckEngine(vehicleReading.getCheckEngine())
                .setBatteryLow(vehicleReading.getBatteryLevel() <= BATTERY_LOW_THRESHOLD)
                .setMotorStatusWarning(vehicleReading.getMotorRpm() >= MOTOR_STATUS_THRESHOLD);
    }
}
