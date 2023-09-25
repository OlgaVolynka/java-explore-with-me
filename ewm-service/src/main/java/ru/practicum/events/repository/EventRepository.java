package ru.practicum.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.util.EventState;
import ru.practicum.util.Pagination;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIdIn(Set<Long> events);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    List<Event> findAllByInitiator_Id(Long userId, Pagination pagination);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "JOIN FETCH e.initiator " +
            "JOIN FETCH e.category " +
            "WHERE e.eventDate > :rangeStart " +
            "AND (e.category.id IN :categories OR :categories IS NULL) " +
            "AND (e.initiator.id IN :users OR :users IS NULL) " +
            "AND (e.state IN :states OR :states IS NULL)"
    )
    List<Event> findAllForAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                LocalDateTime rangeStart, PageRequest pageable);


    @Query("SELECT MIN(e.publishedOn) FROM Event e WHERE e.id IN :eventsId")
    Optional<LocalDateTime> getStart(@Param("eventsId") Collection<Long> eventsId);

    @Query("select e from Event as e " +
            "where e.state = :status " +
            "and (:text is null or lower(e.annotation) like lower(concat('%', :text, '%')) or lower(e.description) like lower(concat('%', :text, '%')))" +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (e.eventDate > :rangeStart) " +
            "and (e.eventDate < :rangeEnd) " +
            "and (:onlyAvailable is null or e.confirmedRequests < e.participantLimit or e.participantLimit = 0)")
    List<Event> findEvents(EventState status,
                           String text,
                           List<Long> categories,
                           Boolean paid,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeEnd,
                           Boolean onlyAvailable,
                           PageRequest pageRequest);
}


