package dding.board.comment.controller;


import dding.board.comment.dto.request.CommentCreateRequest;
import dding.board.comment.dto.response.CommentResponse;
import dding.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(
            @PathVariable("commentId") Long commentId
    )
    {
        return commentService.read(commentId);
    }

    @PostMapping("/v1/comments")
    public CommentResponse create(
            @RequestBody CommentCreateRequest req)
    {
        return commentService.create(req);
    }

    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId)
    {
        commentService.delete(commentId);
    }
}
