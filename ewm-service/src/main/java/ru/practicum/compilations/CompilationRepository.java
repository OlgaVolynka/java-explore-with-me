package ru.practicum.compilations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.util.Pagination;
import org.springframework.data.domain.Page;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinned(Boolean pinned, Pagination pagination);
}
