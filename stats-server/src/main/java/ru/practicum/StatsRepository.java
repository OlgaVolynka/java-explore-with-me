package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.model.ViewStats(" +
            "h.app," +
            "h.uri," +
            "case when :unique = true " +
            "then count (distinct (h.ip))" +
            "else count (h.ip)" +
            "end) " +
            "" +
            "from EndpointHit h " +
            "where h.timestamp between :start and :end " +
            "and :uris is null or h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by 3 desc ")
    List<ViewStats> getStats(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             @Param("uris") List<String> uris,
                             @Param("unique") Boolean unique);

}


