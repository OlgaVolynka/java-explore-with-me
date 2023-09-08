package ru.practicum;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.dto.GetEndpointHitDto;

public class EndpointHitMapper {

    public static EndpointHit endpointHitDtoToEndpointHit(GetEndpointHitDto endpointHitDto) {
        return new EndpointHit(0,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}
