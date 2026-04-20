package ca.nztan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "vehicle_setting")
@Getter
@Accessors(chain = true)
public class VehicleSetting {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "motor_speed", nullable = false)
    private Integer motorSpeed;

    @Column(name = "charging_ind", nullable = false)
    private Boolean chargingOn;
}
