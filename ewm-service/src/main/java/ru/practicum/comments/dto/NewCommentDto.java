package ru.practicum.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCommentDto {

    @NotBlank
    @Size(max = 1000)
    private String text;

}
