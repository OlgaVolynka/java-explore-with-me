package ru.practicum.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.model.Comment;

@UtilityClass
public class CommentMapper {


    public static CommentDto commentToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getText(),
                comment.getEvent().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment newCommentToComment(NewCommentDto newComment) {

        Comment comment = new Comment();

        comment.setText(newComment.getText());

        return comment;
    }

}
