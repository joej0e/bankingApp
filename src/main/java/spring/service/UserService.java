package spring.service;

import spring.dto.RegistrationRequestDto;
import spring.entity.User;
import spring.exception.LoginAlreadyExistsException;

public interface UserService {

    User register(RegistrationRequestDto registrationRequestDto) throws LoginAlreadyExistsException;

    User findByLogin(String login);

    User findById(Long id);

    User save(User user);

}
