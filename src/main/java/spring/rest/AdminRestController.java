package spring.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.UserDto;
import spring.entity.Role;
import spring.entity.User;
import spring.service.RoleService;
import spring.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/admin/")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "users/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable(name = "id") Long id) {
        User result = userService.findById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto userDto = UserDto.fromUser(result);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PatchMapping("users/{id}/add_role")
    public ResponseEntity updateAccount(@PathVariable(name = "id") Long id, @RequestBody Role role) {
        User userToUpdate = userService.findById(id);
        userToUpdate.getRoles().add(roleService.findByName(role.getName()));
        Map<Object, Object> response = new HashMap<>();
        response.put("login", userToUpdate.getLogin());
        response.put("id", userToUpdate.getId());
        List<Role> roles = userToUpdate.getRoles();
        for (Role userRole : roles) {
            response.put("role" + userRole.getId(), userRole.getName());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("status")
    public ResponseEntity getStatus() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<Object> response = new LinkedList<>();
        for(GrantedAuthority authority : authorities) {
            response.add(authority.getAuthority());
        }
        return ResponseEntity.ok(response);
    }
}



