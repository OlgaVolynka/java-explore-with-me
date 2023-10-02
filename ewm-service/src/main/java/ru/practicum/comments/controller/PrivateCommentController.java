package ru.practicum.comments.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.CommentService;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/{userId}/{eventId}")
    public CommentDto createComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid NewCommentDto commentDto) {
        log.info("Create comment by user id:{} and event id:{}", userId, eventId);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{userId}/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid NewCommentDto commentDto) {
        log.info("Пользователь редактирует комментарий");
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/{userId}/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("Delete comment by id:{} .", commentId);
        commentService.authorDeleteComment(userId, commentId);
    }
}
