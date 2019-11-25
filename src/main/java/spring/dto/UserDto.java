package spring.dto;

import lombok.Data;
import spring.entity.User;

@Data
public class UserDto {
    private Long id;
    private String login;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setId(user.getId());
        return userDto;
    }

    public User toUser() {
        User user = new User();
        user.setLogin(login);
        user.setId(id);
        return user;
    }

}

