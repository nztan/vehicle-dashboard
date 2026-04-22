package ca.nztan.backend.service;

import ca.nztan.backend.dto.UserInputDto;
import ca.nztan.backend.entity.UserInput;
import ca.nztan.backend.repository.UserInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInputService {

    private final static int VEHICLE_SETTING_ID = 1;
    private final UserInputRepository userInputRepository;

    public void save(UserInputDto vehicleSetting) {
        userInputRepository.save(toEntity(vehicleSetting));
    }

    public UserInputDto get() {
        return toDto(userInputRepository.findById(VEHICLE_SETTING_ID)
                .orElse(new UserInput()
                        .setMotorSpeed(0)
                        .setChargingOn(false)
                        ));
    }

    private UserInput toEntity(UserInputDto vehicleSetting) {
        return UserInput.newInstance(vehicleSetting.getMotorSpeed(), vehicleSetting.getChargingOn());
    }

    private UserInputDto toDto(UserInput userInput) {
        return new UserInputDto()
                .setMotorSpeed(userInput.getMotorSpeed())
                .setChargingOn(userInput.getChargingOn());
    }
}
