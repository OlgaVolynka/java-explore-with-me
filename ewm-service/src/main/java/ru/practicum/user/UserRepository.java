package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.Pagination;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(List<Long> ids, Pagination pagination);
}
