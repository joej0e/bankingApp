package spring.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.entity.User;
import spring.security.jwt.JwtUser;
import spring.security.jwt.JwtUserFactory;
import spring.service.UserService;

@Service("JwtUserDetailsService")
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        User user = userService.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user: {} successfully loaded", jwtUser);
        return jwtUser;
    }

}
