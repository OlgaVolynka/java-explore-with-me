package ru.practicum.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {

    public static UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName());
    }

    public static UserShortDto userToUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
                );
    }

    public static User newUserRequestToUser(NewUserRequest newUserRequest) {

        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

}
