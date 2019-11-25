package spring.dto;

import lombok.Data;
import spring.validator.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequestDto {
    private String login;

    @NotNull
    @ValidPassword
    private String password;
}
