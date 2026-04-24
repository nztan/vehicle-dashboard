package ca.nztan.backend.service;

import ca.nztan.backend.dto.VehicleSettingDto;
import ca.nztan.backend.entity.VehicleSetting;
import ca.nztan.backend.repository.VehicleSettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleSettingServiceTest {

    @Mock
    private VehicleSettingRepository vehicleSettingRepository;

    @InjectMocks
    private VehicleSettingService vehicleSettingService;

    @Test
    void testSave_updateVehicleSetting_succeeded() {
        UUID vehicleId = UUID.randomUUID();
        VehicleSettingDto input = new VehicleSettingDto()
                .setVehicleId(vehicleId)
                .setMotorSpeed(320)
                .setCharging(true);
        when(vehicleSettingRepository.save(any(VehicleSetting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime before = LocalDateTime.now();
        VehicleSettingDto result = vehicleSettingService.save(input);
        LocalDateTime after = LocalDateTime.now();

        ArgumentCaptor<VehicleSetting> captor = ArgumentCaptor.forClass(VehicleSetting.class);
        verify(vehicleSettingRepository).save(captor.capture());
        VehicleSetting saved = captor.getValue();

        assertThat(saved.getVehicleId()).isEqualTo(vehicleId);
        assertThat(saved.getMotorSpeed()).isEqualTo(320);
        assertThat(saved.getCharging()).isTrue();
        assertThat(saved.getRecordedAt()).isNotNull();
        assertThat(saved.getRecordedAt()).isBetween(before, after);

        assertThat(result.getVehicleId()).isEqualTo(vehicleId);
        assertThat(result.getMotorSpeed()).isEqualTo(320);
        assertThat(result.getCharging()).isTrue();
    }

    @Test
    void testSave_createVehicleSetting_succeeded() {
        VehicleSettingDto input = new VehicleSettingDto()
                .setVehicleId(null)
                .setMotorSpeed(180)
                .setCharging(false);
        when(vehicleSettingRepository.save(any(VehicleSetting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VehicleSettingDto result = vehicleSettingService.save(input);

        ArgumentCaptor<VehicleSetting> captor = ArgumentCaptor.forClass(VehicleSetting.class);
        verify(vehicleSettingRepository).save(captor.capture());
        VehicleSetting saved = captor.getValue();

        assertThat(saved.getVehicleId()).isNotNull();
        assertThat(result.getVehicleId()).isEqualTo(saved.getVehicleId());
        assertThat(result.getMotorSpeed()).isEqualTo(180);
        assertThat(result.getCharging()).isFalse();
    }

    @Test
    void testGet_succeeded() {
        UUID vehicleId = UUID.randomUUID();
        VehicleSetting vehicleSetting = VehicleSetting.builder()
                .vehicleId(vehicleId)
                .motorSpeed(260)
                .charging(true)
                .recordedAt(LocalDateTime.now())
                .build();
        when(vehicleSettingRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleSetting));

        VehicleSettingDto result = vehicleSettingService.get(vehicleId);

        assertThat(result.getVehicleId()).isEqualTo(vehicleId);
        assertThat(result.getMotorSpeed()).isEqualTo(260);
        assertThat(result.getCharging()).isTrue();
    }

    @Test
    void testGet_notExists_failed() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleSettingRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleSettingService.get(vehicleId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testFindAllWithin5Minutes_succeeded() {
        VehicleSetting first = VehicleSetting.builder()
                .vehicleId(UUID.randomUUID())
                .motorSpeed(100)
                .charging(false)
                .recordedAt(LocalDateTime.now().minusMinutes(1))
                .build();
        VehicleSetting second = VehicleSetting.builder()
                .vehicleId(UUID.randomUUID())
                .motorSpeed(200)
                .charging(true)
                .recordedAt(LocalDateTime.now().minusMinutes(2))
                .build();
        when(vehicleSettingRepository.findByRecordedAtAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(first, second));

        LocalDateTime before = LocalDateTime.now().minusMinutes(5);
        List<VehicleSettingDto> result = vehicleSettingService.findAllWithin5Minutes();
        LocalDateTime after = LocalDateTime.now().minusMinutes(5);

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(vehicleSettingRepository).findByRecordedAtAfter(captor.capture());
        assertThat(captor.getValue()).isBetween(before, after);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getVehicleId()).isEqualTo(first.getVehicleId());
        assertThat(result.get(0).getMotorSpeed()).isEqualTo(100);
        assertThat(result.get(0).getCharging()).isFalse();
        assertThat(result.get(1).getVehicleId()).isEqualTo(second.getVehicleId());
        assertThat(result.get(1).getMotorSpeed()).isEqualTo(200);
        assertThat(result.get(1).getCharging()).isTrue();
    }
}
