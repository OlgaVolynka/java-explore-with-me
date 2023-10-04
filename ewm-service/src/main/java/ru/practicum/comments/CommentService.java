package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UnavalibleException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.EventState;
import ru.practicum.util.Pagination;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    //PrivateCommentController methods:
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {


        User user = shackIdUser(userId);
        Event event = shackAndGetEventById(eventId);


        Comment comment = CommentMapper.newCommentToComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = shackAndGetCommentById(commentId);
        shackIdUser(userId);
        shackAuthor(comment, userId);

        comment.setText(newCommentDto.getText());

        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public void authorDeleteComment(Long userId, Long commentId) {
        Comment comment = shackAndGetCommentById(commentId);
        shackIdUser(userId);
        shackAuthor(comment, userId);

        commentRepository.delete(comment);

    }

    //PublicCommentController methods:
    @Transactional(readOnly = true)
    public List<CommentDto> getAllComments(Long eventId, Integer from, Integer size) {
        shackAndGetEventById(eventId);
        PageRequest pageable = new Pagination(from, size, Sort.unsorted());

        return commentRepository
                .findAllByEvent_Id(eventId, pageable)
                .stream()
                .map(CommentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    //AdminCommentController methods:
    public void adminDeleteComment(Long commentId) {
        Comment comment = shackAndGetCommentById(commentId);
        commentRepository.delete(comment);
    }


    //Private methods:
    private User shackIdUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found User by id=" + id));
    }


    private Event shackAndGetEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Event by id=" + id));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UnavalibleException("Event has not published.");
        }
        return event;
    }

    private Comment shackAndGetCommentById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Not found comment by id=" + id));

    }

    private void shackAuthor(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new UnavalibleException("You are not the author of the comment");
        }
    }
}
