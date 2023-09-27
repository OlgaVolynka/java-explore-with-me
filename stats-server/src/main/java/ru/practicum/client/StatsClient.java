package ru.practicum.client;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.GetEndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j

public class StatsClient extends BaseClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    public StatsClient(@Value("&{stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> saveHit(GetEndpointHitDto hit) {
        return post(hit);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        return objectMapper.convertValue(objectResponseEntity.getBody(), new TypeReference<List<ViewStats>>() {

        });
    }
}
