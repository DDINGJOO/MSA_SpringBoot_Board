package dding.board.comment.dto.response;


import dding.board.comment.entity.Comment;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommentResponse {

    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;


    public static CommentResponse from (Comment comment)
    {
        CommentResponse response = new CommentResponse();
        response.articleId = comment.getArticleId();
        response.commentId = comment.getCommentId();
        response.content = comment.getContent();
        response.deleted = comment.getDeleted();
        response.createdAt = comment.getCreatedAt();
        response.parentCommentId = comment.getParentCommentId();
        response.writerId = comment.getWriterId();

        return response;
    }

}
