package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;


public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.model.ViewStats(" +
            "h.app," +
            "h.uri," +
            "count (distinct (h.ip))" +
            ") " +
            "from EndpointHit h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by 3 desc ")
    List<ViewStats> getStatsUnique(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(" +
            "h.app," +
            "h.uri," +
            "count (h.ip)" +
            ") " +
            "" +
            "from EndpointHit h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by 3 desc ")
    List<ViewStats> getStatsNotUnique(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(" +
            "h.app," +
            "h.uri," +
            "count (h.ip)" +
            ") " +
            "" +
            "from EndpointHit h " +
            "where h.timestamp between :start and :end " +
            "group by h.app, h.uri " +
            "order by 3 desc ")
    List<ViewStats> getStatsNotUri(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end
    );
}


