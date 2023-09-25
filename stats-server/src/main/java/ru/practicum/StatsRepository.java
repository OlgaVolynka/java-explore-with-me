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
                                   @Param("uris") String[] uris);

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
                                      @Param("uris") String[] uris);
    /*
  @Query("select new ru.practicum.model.ViewStats(" +
          "h.app," +
          "h.uri," +
          "count (h.ip)" +
          ") " +
          "" +
          "from EndpointHit h " +
          "where h.uri in :uris and h.timestamp between :start and :end " +

          "group by h.app, h.uri " +
          "order by COUNT (h.uri) DESC")
  List<ViewStats> getStatsNotUnique(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") String[] uris);
*/
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
/*
    @Query("SELECT " + NEW_VIEW_STATS + "(h.app, h.uri, COUNT(h.uri)) " + "FROM Hit AS h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.uri) DESC")
    List<ViewStats> findHitsWithUri(String[] uris, LocalDateTime start, LocalDateTime end);
*/
}


