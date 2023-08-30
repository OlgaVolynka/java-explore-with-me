package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "EndpointHit", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Column(name = "app")
    @NotNull
    private String app;
    @Column(name = "uri")
    @NotNull
    private String uri;
    @Column(name = "ip")
    @NotNull
    private String ip;
    @Column(name = "timestamp")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
