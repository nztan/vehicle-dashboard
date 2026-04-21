package ca.nztan.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "user_input")
@Getter
@Accessors(chain = true)
public class UserInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Setter
    @Column(name = "motor_speed", nullable = false)
    private Integer motorSpeed;

    @Setter
    @Column(name = "charging_ind", nullable = false)
    private Boolean chargingOn;

    public static UserInput newInstance(Integer motorSpeed, Boolean chargingOn) {
        UserInput userInput = new UserInput();
        userInput.id = 1;
        userInput.motorSpeed = motorSpeed;
        userInput.chargingOn = chargingOn;
        return userInput;
    }
}
