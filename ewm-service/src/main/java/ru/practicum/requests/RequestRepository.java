package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.util.EventRequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Long countRequestByEventIdAndStatus(Long eventId, EventRequestStatus state);

    List<ParticipationRequest> findAllByEventIdAndIdIn(Long eventId, Set<Long> requestIds);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

}
