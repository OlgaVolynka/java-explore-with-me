package ru.practicum;

import lombok.experimental.UtilityClass;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.dto.GetEndpointHitDto;
import ru.practicum.model.dto.RequestEndpointHitDto;

@UtilityClass
public class EndpointHitMapper {

    public static EndpointHit endpointHitDtoToEndpointHit(GetEndpointHitDto endpointHitDto) {
        return new EndpointHit(0,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }

    public static RequestEndpointHitDto endpointHitToRequestEndpointHitDto(EndpointHit endpointHit) {
        return new RequestEndpointHitDto(
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }
}
