package ca.nztan.backend.controller;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.service.VehicleReadingService;
import ca.nztan.backend.service.VehicleSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleReadingService vehicleReadingService;
    private final VehicleSettingService vehicleSettingService;

    @GetMapping("/snapshot")
    public DashboardSnapshotDto getSnapshot() {
        return vehicleReadingService.getDashboardSnapshot();
    }

    @PostMapping("/setting")
    public VehicleSettingDto save(@RequestBody VehicleSettingDto setting) {
        return vehicleSettingService.save(setting);
    }
}
