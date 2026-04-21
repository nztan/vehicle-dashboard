package ca.nztan.backend.simulator;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleSimulatorState {
    private int motorRpm;
    private int powerKw;
    private int batteryLevel;
    private int batteryTemperature;
}
