package ca.nztan.backend.controller;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.dto.VehicleSettingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    @GetMapping("snapshot")
    public DashboardSnapshotDto getSnapshot() {
        return new DashboardSnapshotDto()
                .setMotorRPM(500)
                .setPowerKw(250)
                .setBatteryLevel(60)
                .setBatteryTemperature(25)
                .setCheckEngine(true)
                .setParkingBrake(false)
                .setRecordedAt(LocalDate.now());
    }

    @PostMapping
    public VehicleSettingDto set(@RequestBody VehicleSettingDto setting) {
        return setting;
    }
}
