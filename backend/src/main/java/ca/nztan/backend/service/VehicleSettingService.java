package ca.nztan.backend.service;

import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.entity.VehicleSetting;
import ca.nztan.backend.repository.VehicleSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleSettingService {

    private final static int VEHICLE_SETTING_ID = 1;
    private final VehicleSettingRepository vehicleSettingRepository;

    public VehicleSettingDto save(VehicleSettingDto vehicleSetting) {
        return toDto(vehicleSettingRepository.save(toEntity(vehicleSetting)));
    }

    public VehicleSettingDto get() {
        return vehicleSettingRepository.findById(VEHICLE_SETTING_ID)
                .map(this::toDto)
                .orElseGet(this::getDefaultVehicleSetting);
    }

    private VehicleSettingDto getDefaultVehicleSetting() {
        return new VehicleSettingDto()
                .setMotorSpeed(0)
                .setCharging(false);
    }

    private VehicleSetting toEntity(VehicleSettingDto vehicleSetting) {
        return VehicleSetting.builder()
                .id(VEHICLE_SETTING_ID)
                .motorSpeed(vehicleSetting.getMotorSpeed())
                .chargingOn(vehicleSetting.getCharging())
                .build();
    }

    private VehicleSettingDto toDto(VehicleSetting vehicleSetting) {
        return new VehicleSettingDto()
                .setMotorSpeed(vehicleSetting.getMotorSpeed())
                .setCharging(vehicleSetting.getChargingOn());
    }
}
