package ca.nztan.backend.controller;

import ca.nztan.backend.dto.DashboardSnapshotDto;
import ca.nztan.backend.dto.UserInputDto;
import ca.nztan.backend.service.UserInputService;
import ca.nztan.backend.service.VehicleReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final UserInputService userInputService;
    private final VehicleReadingService vehicleReadingService;

    @GetMapping("/snapshot")
    public DashboardSnapshotDto getSnapshot() {
        return vehicleReadingService.getLatestDashboardSnapshot();
    }

    @PostMapping("/setting")
    public void save(@RequestBody UserInputDto setting) {
        userInputService.save(setting);
    }
}
