package ca.nztan.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleSettingDto {
    private Integer motorSpeed;
    private Boolean chargingOn;
}
