package ru.practicum.compilations;

import org.springframework.data.domain.Sort;
import ru.practicum.compilations.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.Pagination;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    //AdminCompilationController:
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.compilationDtoToNewCompilation(dto);

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        } else {
            compilation.setPinned(false);
        }

        Set<Long> eventsId = dto.getEvents();
        if (eventsId != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllByIdIn(eventsId));
            compilation.setEvents(events);
        }

        Compilation newCompilation = compilationRepository.save(compilation);

        return CompilationMapper.compilationToCompilationDto(newCompilation);
    }

    @Transactional
    public CompilationDto updateCompilationById(Long compId, CompilationUpdatedDto dto) {
        Compilation newCompilation = getAndShackCompilation(compId);

        if (dto.getTitle() != null) {
            newCompilation.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            newCompilation.setPinned(dto.getPinned());
        }

        if (dto.getEvents() != null) {
            Set<Long> eventId = dto.getEvents();
            Collection<Event> events = eventRepository.findAllByIdIn(eventId);
            newCompilation.setEvents(new HashSet<>(events));
        }

        return CompilationMapper.compilationToCompilationDto(newCompilation);
    }

    @Transactional
    public void deleteCompilationById(Long compId) {
        getAndShackCompilation(compId);
        compilationRepository.deleteById(compId);
    }


    //PublicCompilationController:

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {

        if (pinned == null) {
            return compilationRepository.findAll(new Pagination(from, size, Sort.unsorted())).getContent().stream()
                    .map(CompilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        }

        return compilationRepository.findAllByPinned(pinned, new Pagination(from, size, Sort.unsorted()))
                .getContent().stream()
                .map(CompilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = getAndShackCompilation(id);

        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    //PrivateMethods

    private Compilation getAndShackCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Compilation id:" + id));
    }
}
