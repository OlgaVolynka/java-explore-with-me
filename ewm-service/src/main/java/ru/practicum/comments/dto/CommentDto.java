package ru.practicum.comments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private String text;
    private Long eventId;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();

}
