package dding.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepository {

    private final StringRedisTemplate redisTemplate;

    // hot-article::Article::{articleID}::like-count;
    private static final String KET_FORMAT = "hot-article::Article::%s::like-count";

    public void createOrUpdate(Long articleId, Long likeCount, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(likeCount), ttl);
    }

    public Long read(Long articleId) {
        var result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public String generateKey(Long articleId) {
        return KET_FORMAT.formatted(articleId);
    }

}
