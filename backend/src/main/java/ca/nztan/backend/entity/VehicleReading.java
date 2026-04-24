package ca.nztan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "vehicle_reading", schema = "vehicle_dashboard")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "vehicleId")
@Accessors(chain = true)
public class VehicleReading {

    @Id
    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

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
    @Column(name = "gear_ratio", nullable = false)
    private String gearRatio;

    @Setter
    @Column(name = "parking_brake_ind", nullable = false)
    private Boolean parkingBrake;

    @Setter
    @Column(name = "check_engine_ind", nullable = false)
    private Boolean checkEngine;
}
