package dding.board.comment.dto.response;


import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CommentPageResponse {

    private List<CommentResponse> comments;
    private Long commentCount;


    public  static CommentPageResponse of(List<CommentResponse> comments, Long commentCount)
    {
        CommentPageResponse response = new CommentPageResponse();
        response.comments = comments;
        response.commentCount = commentCount;
        return response;
    }
}
