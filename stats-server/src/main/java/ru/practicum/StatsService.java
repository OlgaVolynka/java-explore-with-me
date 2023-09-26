package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataNotCorrectException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.GetEndpointHitDto;
import ru.practicum.model.dto.RequestEndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public RequestEndpointHitDto create(GetEndpointHitDto hit) {
        EndpointHit newEndpointHit = statsRepository.save(EndpointHitMapper.endpointHitDtoToEndpointHit(hit));
        return EndpointHitMapper.endpointHitToRequestEndpointHitDto(newEndpointHit);
    }

    public List<ViewStats> getHit(LocalDateTime start, LocalDateTime end, String[] uri, Boolean unique) {

        if (start == null || end == null) throw new DataNotFoundException("Start or and not present");
        if (start.isAfter(end)) throw new DataNotCorrectException("Start should be before and");

        if (uri == null) return statsRepository.getStatsNotUri(start, end);
        else if (unique) return statsRepository.getStatsUnique(start, end, uri);
        else {
            return statsRepository.getStatsNotUnique(start, end, uri);
        }

    }
}
