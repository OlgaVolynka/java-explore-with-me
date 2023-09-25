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
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
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
        //    ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&unique={unique}", parameters);

        return objectMapper.convertValue(objectResponseEntity.getBody(), new TypeReference<List<ViewStats>>() {

        });
    }
}




    /*
  protected final  RestTemplate restTemplate;


   public StatsClient(@Value("http://localhost:9090") String baseUrl, RestTemplateBuilder builder) {
            this.restTemplate = builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build();
        }

    public void saveStats(String app, String uri, String ip, LocalDateTime timestamp) {

        restTemplate.put("/hit",GetEndpointHitDto.class);
              /*  .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GetEndpointHitDto(app, uri, ip, timestamp))
                .retrieve()
                .toBodilessEntity()
                //.doOnNext(c -> log.info("Save stats"))
                .block();*/
/* }*/

/*
    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }


    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }
*/

    /*
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {

        try{
            ResponseEntity<List<ViewStats>> response = this.restTemplate.getForObject("/stats", ViewStats.class);
return response.getBody();
        } catch (HttpStatusCodeException e){
            throw new RuntimeException(" error");
        }
    }
*/
 /*   private final WebClient client;

   // public StatsClient(@Value("${stats.server.url}") String baseUrl) {
   public StatsClient(@Value("http://localhost:9090") String baseUrl) {
    this.client = WebClient.create(baseUrl);
    }

    public ResponseEntity<List<ViewStats>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStats.class)
                .doOnNext(c -> log.info("Get stats with param: start date {}, end date {}, uris {}, unique {}",
                        start, end, uris, unique))
                .block();
    }

    public void saveStats(String app, String uri, String ip, LocalDateTime timestamp) {

        this.client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GetEndpointHitDto(app, uri, ip, timestamp))
                .retrieve()
                .toBodilessEntity()
                .doOnNext(c -> log.info("Save stats"))
                .block();
    }


    }

  */
