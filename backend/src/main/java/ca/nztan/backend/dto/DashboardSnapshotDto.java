package ca.nztan.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DashboardSnapshotDto {
    private Integer motorRPM;
    private Integer powerKw;
    private Integer batteryLevel;
    private Integer batteryTemperature;
    private Boolean parkingBrake;
    private Boolean checkEngine;
}
