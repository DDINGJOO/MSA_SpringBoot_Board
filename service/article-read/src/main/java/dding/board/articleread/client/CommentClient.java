package dding.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentClient {

    private RestClient restClient;

    @Value("${endpoint.dding-board-comment-service-url}")
    private String commentServiceUrl;


    @PostConstruct
    public void initRestClient()
    {
        restClient = RestClient.create(commentServiceUrl);
    }

    public Long count(Long articleId)
    {
        try{
            return  restClient.get()
                    .uri("/v1/comments/articles/{articleId}/count}",articleId)
                    .retrieve()
                    .body(Long.class);
        }catch (Exception e)
        {
            log.error("[CommentClient.count] commentId = {} ",articleId,e);
            return  0L;
        }
    }
}