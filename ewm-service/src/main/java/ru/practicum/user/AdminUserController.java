package ru.practicum.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Create user {}", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    @GetMapping()
    public List<UserDto> get(@RequestParam(defaultValue = "") List<Long> ids,
                             @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                             @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get users with ids: {}", ids);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        log.info("Delete user id= {}", userId);
        userService.deleteUserById(userId);
    }
}
