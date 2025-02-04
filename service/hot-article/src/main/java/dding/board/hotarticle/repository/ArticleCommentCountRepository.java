package dding.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentCountRepository {
    private final StringRedisTemplate redisTemplate;

    //hot-article::article::{articleId}::comment-count
    private static final String KET_FORMAT = "hot-article::Article::%s::comment-count";



    public void createOrUpdate(Long articleId, Long commentCount, Duration ttl)
    {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(commentCount), ttl);
    }


    public Long read(Long articleId)
    {
        var result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }
    public String generateKey(Long articleId)
    {
        return KET_FORMAT.formatted((articleId));
    }

}
