package ca.nztan.backend.simulator;

import ca.nztan.backend.dto.GearType;
import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.service.VehicleReadingService;
import ca.nztan.backend.service.VehicleSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleSimulator {

    private static final int[] MOTOR_RPM_BY_SPEED = {0, 200, 400, 600, 800};

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
    private VehicleState previousState;

    @Scheduled(fixedRate = 1200)
    public void simulate() {
        VehicleSettingDto userInput = vehicleSettingService.get();
        if (previousState == null) {
            previousState = getDefaultState();
        }
        // reset motor speed when battery is empty or charging
        if (previousState.getBatteryLevel() == 0 || userInput.getChargingOn()) {
            userInput.setMotorSpeed(0);
        }

        // set up motorRPM boundary with motor speed, powerKW varies with the motorRPM
        int targetMotorRpm = MOTOR_RPM_BY_SPEED[userInput.getMotorSpeed()];
        int targetPowerKW = userInput.getChargingOn() ? MIN_POWER_KW : (targetMotorRpm * MAX_POWER_KW / MOTOR_RPM_BY_SPEED[MOTOR_RPM_BY_SPEED.length - 1]);

        VehicleState newState = new VehicleState()
                .setMotorRpm(interpolate(previousState.getMotorRpm(), targetMotorRpm, MOTOR_RPM_STEP))
                .setPowerKw(interpolate(previousState.getPowerKw(), targetPowerKW, POWER_KW_STEP))
                .setBatteryLevel(calculateBatteryLevel(userInput, previousState.getBatteryLevel()))
                .setBatteryTemperature(calculateBatteryTemperature(userInput, previousState.getBatteryTemperature()));
        newState.setGear(newState.getMotorRpm() == 0 ? GearType.P : GearType.D);
        vehicleReadingService.save(newState);
        previousState = newState;

        log.debug("Simulated vehicle with user input: {}, and state updated: {}", userInput, newState);
    }

    private VehicleState getDefaultState() {
        // default as a parked vehicle, with full battery and no speed
        return new VehicleState()
                .setMotorRpm(0)
                .setPowerKw(0)
                .setBatteryLevel(100)
                .setBatteryTemperature(25)
                .setGear(GearType.P);
    }

    private int calculateBatteryTemperature(VehicleSettingDto userInput, int current) {
        // battery temperature changes at high speed only
        if (userInput.getMotorSpeed() > 2) {
            return Math.min(current + BATTERY_TEMPERATURE_STEP, MAX_BATTERY_TEMPERATURE);
        }
        return Math.max(current - BATTERY_TEMPERATURE_STEP, MIN_BATTERY_TEMPERATURE);
    }

    private int calculateBatteryLevel(VehicleSettingDto userInput, int current) {
        // battery level changes only when charging or moving
        if (userInput.getChargingOn()) {
            return Math.min(current + BATTERY_LEVEL_STEP, MAX_BATTERY_LEVEL);
        } else if (userInput.getMotorSpeed() == 0) {
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
