package ca.nztan.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DashboardSnapshotDto {
    private VehicleReadingDto vehicleReading;
    private VehicleSettingDto vehicleSetting;
}
