package ca.nztan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle_reading")
@Getter
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class VehicleReading {

    @Id
    @Column(name = "id", nullable = false)
    private BigDecimal id;

    @Setter
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
    private Boolean parkingBradeOn;

    @Setter
    @Column(name = "check_engine_ind", nullable = false)
    private Boolean checkEngineOn;
}
