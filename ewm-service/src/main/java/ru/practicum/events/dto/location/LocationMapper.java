package ru.practicum.events.dto.location;

import lombok.experimental.UtilityClass;
import ru.practicum.events.model.Location;

@UtilityClass
public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }


    public static Location dtoToLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}
