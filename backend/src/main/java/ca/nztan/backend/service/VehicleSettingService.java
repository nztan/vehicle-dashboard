package ca.nztan.backend.service;

import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.entity.VehicleSetting;
import ca.nztan.backend.repository.VehicleSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleSettingService {

    private final VehicleSettingRepository vehicleSettingRepository;

    public VehicleSettingDto save(VehicleSettingDto vehicleSetting) {
        return toDto(vehicleSettingRepository.save(toEntity(vehicleSetting)));
    }

    public VehicleSettingDto get(UUID vehicleId) {
        return vehicleSettingRepository.findById(vehicleId)
                .map(this::toDto)
                .orElseThrow();
    }

    public List<VehicleSettingDto> findAllWithin5Minutes() {
        return vehicleSettingRepository.findByRecordedAtAfter(LocalDateTime.now().minusMinutes(5))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private VehicleSetting toEntity(VehicleSettingDto vehicleSetting) {
        return VehicleSetting.builder()
                .vehicleId(vehicleSetting.getVehicleId() == null ? UUID.randomUUID() : vehicleSetting.getVehicleId())
                .motorSpeed(vehicleSetting.getMotorSpeed())
                .charging(vehicleSetting.getCharging())
                .recordedAt(LocalDateTime.now())
                .build();
    }

    private VehicleSettingDto toDto(VehicleSetting vehicleSetting) {
        return new VehicleSettingDto()
                .setVehicleId(vehicleSetting.getVehicleId())
                .setMotorSpeed(vehicleSetting.getMotorSpeed())
                .setCharging(vehicleSetting.getCharging());
    }
}
