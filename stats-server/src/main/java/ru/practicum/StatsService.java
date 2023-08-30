package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public EndpointHit create(EndpointHit hit) {

        return statsRepository.save(hit);

    }

    @Transactional
    public List<ViewStats> getHit(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {

        return statsRepository.getStats(start, end, uri, unique);

    }
}
