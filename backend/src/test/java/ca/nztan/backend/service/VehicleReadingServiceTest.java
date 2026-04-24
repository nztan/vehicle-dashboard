package ca.nztan.backend.service;

import ca.nztan.backend.dto.VehicleReadingDto;
import ca.nztan.backend.entity.VehicleReading;
import ca.nztan.backend.repository.VehicleReadingRepository;
import ca.nztan.backend.simulator.VehicleState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleReadingServiceTest {

    @Mock
    private VehicleReadingRepository vehicleReadingRepository;

    @InjectMocks
    private VehicleReadingService vehicleReadingService;

    @Test
    void testGet_notExists_succeeded() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleReadingRepository.findById(vehicleId)).thenReturn(Optional.empty());

        VehicleReadingDto result = vehicleReadingService.get(vehicleId);

        assertThat(result.getMotorRPM()).isEqualTo(0);
        assertThat(result.getPowerKw()).isEqualTo(0);
        assertThat(result.getBatteryLevel()).isEqualTo(0);
        assertThat(result.getBatteryTemperature()).isEqualTo(0);
        assertThat(result.getGearRatio()).isEqualTo("N/N");
        assertThat(result.getParkingBrake()).isFalse();
        assertThat(result.getCheckEngine()).isFalse();
        assertThat(result.getBatteryLow()).isFalse();
        assertThat(result.getMotorStatusWarning()).isFalse();
    }

    @Test
    void testGet_exists_succeeded() {
        UUID vehicleId = UUID.randomUUID();
        VehicleReading vehicleReading = VehicleReading.builder()
                .vehicleId(vehicleId)
                .motorRpm(500)
                .powerKw(120)
                .batteryLevel(20)
                .batteryTemperature(36)
                .gearRatio("4/6")
                .parkingBrake(false)
                .checkEngine(false)
                .build();
        when(vehicleReadingRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleReading));

        VehicleReadingDto result = vehicleReadingService.get(vehicleId);

        assertThat(result.getMotorRPM()).isEqualTo(500);
        assertThat(result.getPowerKw()).isEqualTo(120);
        assertThat(result.getBatteryLevel()).isEqualTo(20);
        assertThat(result.getBatteryTemperature()).isEqualTo(36);
        assertThat(result.getGearRatio()).isEqualTo("4/6");
        assertThat(result.getParkingBrake()).isFalse();
        assertThat(result.getCheckEngine()).isFalse();
        assertThat(result.getBatteryLow()).isTrue();
        assertThat(result.getMotorStatusWarning()).isTrue();
    }

    @Test
    void testSave_succeeded() {
        UUID vehicleId = UUID.randomUUID();
        VehicleState state = new VehicleState()
                .setMotorRpm(800)
                .setPowerKw(150)
                .setBatteryLevel(65)
                .setBatteryTemperature(30)
                .setGearRatio("N/N");

        vehicleReadingService.save(vehicleId, state);

        ArgumentCaptor<VehicleReading> captor = ArgumentCaptor.forClass(VehicleReading.class);
        verify(vehicleReadingRepository).save(captor.capture());
        VehicleReading saved = captor.getValue();

        assertThat(saved.getVehicleId()).isEqualTo(vehicleId);
        assertThat(saved.getMotorRpm()).isEqualTo(800);
        assertThat(saved.getPowerKw()).isEqualTo(150);
        assertThat(saved.getBatteryLevel()).isEqualTo(65);
        assertThat(saved.getBatteryTemperature()).isEqualTo(30);
        assertThat(saved.getGearRatio()).isEqualTo("N/N");
        assertThat(saved.getParkingBrake()).isTrue();
        assertThat(saved.getCheckEngine()).isTrue();
    }
}
