package spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.validator.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RegistrationRequestDto {

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @ValidPassword
    private String password;
}
