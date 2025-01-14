package dding.board.comment.api;

import dding.board.comment.dto.response.CommentResponse;
import dding.board.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {

    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment((new CommentCreateRequest(1L,"mycomment1", null, 1L)));
        CommentResponse response2 = createComment((new CommentCreateRequest(1L,"mycomment2", response1.getCommentId(), 1L)));
        CommentResponse response3 = createComment((new CommentCreateRequest(1L,"mycomment3", response1.getCommentId(), 1L)));


        System.out.println("commentId = %s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId = %s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId = %s".formatted(response3.getCommentId()));



//        commentId = 137467320494878720
//        commentId = 137467321367293952
//        commentId = 137467321438597120
    }

    CommentResponse createComment(CommentCreateRequest req)
    {
        return restClient.post()
                .uri("/v1/comments")
                .body(req)
                .retrieve()
                .body(CommentResponse.class);
    }



    @Test
    void read()
    {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}",137467321438597120L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response : " + response);
    }

    @Test
    void delete()
    {
        restClient.delete().uri("/v1/comments/{commentId}", 137467321438597120L)
                .retrieve();
    }


    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}


