package spring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.dto.RegistrationRequestDto;
import spring.entity.User;
import spring.exception.LoginAlreadyExistsException;
import spring.repository.RoleRepository;
import spring.repository.UserRepository;
import spring.service.UserService;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static AtomicInteger REGISTER_COUNTER = new AtomicInteger(0);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User register(RegistrationRequestDto newUser) throws LoginAlreadyExistsException {
        if (userRepository.findByLogin(newUser.getLogin()) != null) {
            throw new LoginAlreadyExistsException("User with login: " + newUser.getLogin() + " already exists!");
        }
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        user.setLogin(newUser.getLogin());
        if (REGISTER_COUNTER.get() == 0) {
            user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_ADMIN")));
            REGISTER_COUNTER.set(1);
        } else {
            user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
            REGISTER_COUNTER.getAndIncrement();
        }
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered ", registeredUser);
        return registeredUser;
    }

    @Override
    public User findByLogin(String login) {
        User result = userRepository.findByLogin(login);
        log.info("IN findByLogin - user: {} successfully registered ", result);
        return result;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
