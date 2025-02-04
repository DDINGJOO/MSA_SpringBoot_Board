package dding.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class ArticleViewCountRepository {
    private  final StringRedisTemplate redisTemplate;
    // hot-article::Article::{articleID}::view-count;
    private static final String KET_FORMAT = "hot-article::Article::%s::view-count";

    public void createOrUpdate(Long articleId, Long viewCount, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(viewCount), ttl);
    }

    public Long read(Long articleId) {
        var result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public String generateKey(Long articleId) {
        return KET_FORMAT.formatted(articleId);
    }

}
