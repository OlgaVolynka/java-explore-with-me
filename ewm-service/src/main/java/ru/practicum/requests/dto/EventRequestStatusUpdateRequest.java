package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private EventRequestStatus status;

    public enum EventRequestStatus {
        CONFIRMED,
        REJECTED
    }
}
