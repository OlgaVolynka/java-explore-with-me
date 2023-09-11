package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "EndpointHit", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class EndpointHit {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
