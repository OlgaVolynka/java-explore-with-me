package ru.practicum.user;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = userRepository.save(UserMapper.newUserRequestToUser(newUserRequest));
        return UserMapper.userToUserDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {

        if (ids.isEmpty()) {
            return userRepository.findAll(new Pagination(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, new Pagination(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    public void deleteUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found User id" + userId));
        userRepository.deleteById(userId);
    }
}
