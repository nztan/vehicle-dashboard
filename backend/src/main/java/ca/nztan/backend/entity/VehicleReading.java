package ca.nztan.backend.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(name = "vehicle_reading")
@Getter
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class VehicleReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "recorded_at", nullable = false)
    private LocalDate recordedAt;

    @Setter
    @Column(name = "motor_rpm", nullable = false)
    private Integer motorRpm;

    @Setter
    @Column(name = "power_kw", nullable = false)
    private Integer powerKw;

    @Setter
    @Column(name = "battery_level", nullable = false)
    private Integer batteryLevel;

    @Setter
    @Column(name = "battery_temperature", nullable = false)
    private Integer batteryTemperature;

    @Setter
    @Column(name = "parking_brake_ind", nullable = false)
    private Boolean parkingBrade;

    @Setter
    @Column(name = "check_engine_ind", nullable = false)
    private Boolean checkEngine;

    public static VehicleReading newInstance(int motorRpm, int powerKw, int batteryLevel, int batteryTemperature, boolean parkingBrade, boolean checkEngine) {
        VehicleReading vehicleReading = new VehicleReading();
        vehicleReading.id = null;
        vehicleReading.motorRpm = motorRpm;
        vehicleReading.powerKw = powerKw;
        vehicleReading.batteryLevel = batteryLevel;
        vehicleReading.batteryTemperature = batteryTemperature;
        vehicleReading.parkingBrade = parkingBrade;
        vehicleReading.checkEngine = checkEngine;
        return vehicleReading;
    }
}
