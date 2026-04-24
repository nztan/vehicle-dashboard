package ca.nztan.backend.simulator;

import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.service.VehicleReadingService;
import ca.nztan.backend.service.VehicleSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleSimulator {

    private final static int[] MOTOR_RPM_BY_SPEED = {0, 200, 400, 600, 800};
    private final static String GEAR_RATIO_PARK = "N/N";
    private final static String GEAR_RATIO_MOVING = "1/1";

    private final static int MAX_POWER_KW = 1000;
    private final static int MAX_BATTERY_LEVEL = 100;
    private final static int MAX_BATTERY_TEMPERATURE = 100;

    private final static int MIN_POWER_KW = -1000;
    private final static int MIN_BATTERY_LEVEL = 0;
    private final static int MIN_BATTERY_TEMPERATURE = 20;

    private final static int MOTOR_RPM_STEP = 50;
    private final static int POWER_KW_STEP = 100;
    private final static int BATTERY_LEVEL_STEP = 1;
    private final static int BATTERY_TEMPERATURE_STEP = 1;

    private final VehicleReadingService vehicleReadingService;
    private final VehicleSettingService vehicleSettingService;
    private final Map<UUID, VehicleState> previousStates = new HashMap<>();

    @Scheduled(fixedRate = 2000)
    public void simulate() {
        List<VehicleSettingDto> vehicleSettings = vehicleSettingService.findAllWithin5Minutes();
        vehicleSettings.forEach(this::simulateVehicle);
    }

    private void simulateVehicle(VehicleSettingDto vehicleSetting) {
        VehicleState previousState = previousStates.getOrDefault(vehicleSetting.getVehicleId(), getDefaultState());

        // reset motor speed when battery is empty or charging
        if (previousState.getBatteryLevel() == 0 || vehicleSetting.getCharging()) {
            vehicleSetting.setMotorSpeed(0);
        }

        // set up motorRPM boundary with motor speed, powerKW varies with the motorRPM
        int targetMotorRpm = MOTOR_RPM_BY_SPEED[vehicleSetting.getMotorSpeed()];
        int targetPowerKW = vehicleSetting.getCharging() ? MIN_POWER_KW : (targetMotorRpm * MAX_POWER_KW / MOTOR_RPM_BY_SPEED[MOTOR_RPM_BY_SPEED.length - 1]);

        VehicleState newState = new VehicleState()
                .setMotorRpm(interpolate(previousState.getMotorRpm(), targetMotorRpm, MOTOR_RPM_STEP))
                .setPowerKw(interpolate(previousState.getPowerKw(), targetPowerKW, POWER_KW_STEP))
                .setBatteryLevel(calculateBatteryLevel(vehicleSetting, previousState.getBatteryLevel()))
                .setBatteryTemperature(calculateBatteryTemperature(vehicleSetting, previousState.getBatteryTemperature()));
        newState.setGearRatio(vehicleSetting.getMotorSpeed() == 0 ? GEAR_RATIO_PARK : GEAR_RATIO_MOVING);
        vehicleReadingService.save(vehicleSetting.getVehicleId(), newState);
        previousStates.put(vehicleSetting.getVehicleId(), newState);

        if (vehicleSetting.getCharging()) {
            log.debug("Vehicle {} is charging, vehicle state: {}", vehicleSetting.getVehicleId(), newState);
        } else {
            log.debug("Vehicle {} is moving in {}, vehicle state {}", vehicleSetting.getVehicleId(), vehicleSetting.getMotorSpeed(), newState);
        }
    }

    private VehicleState getDefaultState() {
        // default as a parked vehicle, with full battery and no speed
        return new VehicleState()
                .setMotorRpm(0)
                .setPowerKw(0)
                .setBatteryLevel(100)
                .setBatteryTemperature(25)
                .setGearRatio(GEAR_RATIO_PARK);
    }

    private int calculateBatteryTemperature(VehicleSettingDto vehicleSetting, int current) {
        // battery temperature changes at high speed only
        if (vehicleSetting.getMotorSpeed() > 2) {
            return Math.min(current + BATTERY_TEMPERATURE_STEP, MAX_BATTERY_TEMPERATURE);
        }
        return Math.max(current - BATTERY_TEMPERATURE_STEP, MIN_BATTERY_TEMPERATURE);
    }

    private int calculateBatteryLevel(VehicleSettingDto vehicleSetting, int current) {
        // battery level changes only when charging or moving
        if (vehicleSetting.getCharging()) {
            return Math.min(current + BATTERY_LEVEL_STEP, MAX_BATTERY_LEVEL);
        } else if (vehicleSetting.getMotorSpeed() == 0) {
            return current;
        }
        return Math.max(current - BATTERY_LEVEL_STEP, MIN_BATTERY_LEVEL);
    }

    private int interpolate(int current, int target, int step) {
        if (current < target) {
            return Math.min(current + step, target);
        } else if (current > target) {
            return Math.max(current - step, target);
        }
        return current;
    }
}
