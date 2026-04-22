package ca.nztan.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "vehicle_setting")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VehicleSetting {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Setter
    @Column(name = "motor_speed", nullable = false)
    private Integer motorSpeed;

    @Setter
    @Column(name = "charging_ind", nullable = false)
    private Boolean chargingOn;
}
