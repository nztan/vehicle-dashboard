package ca.nztan.backend.controller;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.dto.VehicleReadingDto;
import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.service.VehicleReadingService;
import ca.nztan.backend.service.VehicleSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleReadingService vehicleReadingService;
    private final VehicleSettingService vehicleSettingService;

    @GetMapping("/snapshot/{vehicleId}")
    public DashboardSnapshotDto getSnapshot(@PathVariable UUID vehicleId) {
        VehicleReadingDto dashboard = vehicleReadingService.get(vehicleId);
        VehicleSettingDto vehicleSetting = vehicleSettingService.get(vehicleId);
        return toDashboardSnapshotDto(dashboard, vehicleSetting);
    }

    @PostMapping("/setting")
    public VehicleSettingDto createSetting(@RequestBody VehicleSettingDto setting) {
        return vehicleSettingService.save(setting);
    }

    @PutMapping("/setting/{vehicleId}")
    public VehicleSettingDto updateSetting(@PathVariable UUID vehicleId, @RequestBody VehicleSettingDto setting) {
        setting.setVehicleId(vehicleId);
        return vehicleSettingService.save(setting);
    }

    private DashboardSnapshotDto toDashboardSnapshotDto(VehicleReadingDto dashboard, VehicleSettingDto vehicleSetting) {
        return new DashboardSnapshotDto()
                .setVehicleReading(dashboard)
                .setVehicleSetting(vehicleSetting);
    }
}
