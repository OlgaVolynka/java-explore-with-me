package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.user.model.User;

import static ru.practicum.util.EventRequestStatus.CONFIRMED;

@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequestDto requestToParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static ParticipationRequest createNewParticipationRequest(Event event, User user) {
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        }
        return request;
    }

}
