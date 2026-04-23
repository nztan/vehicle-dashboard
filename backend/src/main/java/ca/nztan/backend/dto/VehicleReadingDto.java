package ca.nztan.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleReadingDto {
    private Integer motorRPM;
    private Integer powerKw;
    private Integer batteryLevel;
    private Integer batteryTemperature;
    private String gearRatio;
    private Boolean parkingBrake;
    private Boolean checkEngine;
    private Boolean batteryLow;
    private Boolean motorStatusWarning;
}
