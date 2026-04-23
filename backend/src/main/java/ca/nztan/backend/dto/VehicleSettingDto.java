package ca.nztan.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class VehicleSettingDto {
    private UUID vehicleId;
    private Integer motorSpeed;
    private Boolean charging;
}
