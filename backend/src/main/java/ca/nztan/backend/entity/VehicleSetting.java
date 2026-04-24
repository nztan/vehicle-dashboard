package ca.nztan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_setting", schema = "vehicle_dashboard")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "vehicleId")
@Accessors(chain = true)
public class VehicleSetting {

    @Id
    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Setter
    @Column(name = "motor_speed", nullable = false)
    private Integer motorSpeed;

    @Setter
    @Column(name = "charging_ind", nullable = false)
    private Boolean charging;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
