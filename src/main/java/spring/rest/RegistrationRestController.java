package spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.RegistrationRequestDto;
import spring.exception.LoginAlreadyExistsException;
import spring.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RegistrationRestController {

    private final UserService userService;

    @Autowired
    public RegistrationRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("registration")
    public ResponseEntity register(@RequestBody @Valid RegistrationRequestDto registrationRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<? super FieldError> fieldErrors = bindingResult.getAllErrors()
                    .stream()
                    .filter(e -> e instanceof FieldError)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.register(registrationRequestDto);
        } catch (LoginAlreadyExistsException e) {
            return new ResponseEntity<>("Login " + registrationRequestDto.getLogin()
                    + " already exists!", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok("You have successfully registered");
    }
}
