package dding.board.comment.api;

import dding.board.comment.dto.response.CommentPageResponse;
import dding.board.comment.dto.response.CommentResponse;
import dding.board.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.mbeans.ContextResourceLinkMBean;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiTest {

    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment((new CommentCreateRequest(1L,"mycomment1", null, 1L)));
        CommentResponse response2 = createComment((new CommentCreateRequest(1L,"mycomment2", response1.getCommentId(), 1L)));
        CommentResponse response3 = createComment((new CommentCreateRequest(1L,"mycomment3", response1.getCommentId(), 1L)));


        System.out.printf("commentId = %s%n", response1.getCommentId());
        System.out.printf("\tcommentId = %s%n", response2.getCommentId());
        System.out.printf("\tcommentId = %s%n", response3.getCommentId());

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
                .uri("/v1/comments/{commentId}",137772565655441408L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response : " + response);
    }

    @Test
    void readAll()
    {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);


        System.out.println("response.getCommentCount() = "+ response.getCommentCount());
        for(CommentResponse comment: response.getComments())
        {
            System.out.println("comment = " + comment.getCommentId());
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : responses1) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        Long lastParentCommentId = responses1.getLast().getParentCommentId();
        Long lastCommentId = responses1.getLast().getCommentId();

        List<CommentResponse> responses2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s"
                        .formatted(lastParentCommentId, lastCommentId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse comment : responses2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }

    @Test
    void delete()
    {
        restClient.delete().uri("/v1/comments/{commentId}", 137772565655441408L)
                .retrieve();
    }

    @Test
    void count()
    {
        CommentResponse response1 = createComment((new CommentCreateRequest(2L,"mycomment1", null, 1L)));
        var count1 = restClient.get()
                .uri("/v1/comments/articles/{articleId}/count",response1.getArticleId())
                .retrieve()
                .body(Long.class);

        System.out.println("count1 = "+count1);
        restClient.delete().uri("/v1/comments/{commentId}", response1.getCommentId())
                .retrieve();

        var count2 = restClient.get()
                .uri("/v1/comments/articles/{articleId}/count",response1.getArticleId())
                .retrieve()
                .body(Long.class);

        System.out.println("count2 = " +count2);
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


