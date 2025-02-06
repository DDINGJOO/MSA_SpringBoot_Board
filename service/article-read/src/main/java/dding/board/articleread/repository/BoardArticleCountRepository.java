package dding.board.articleread.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@RequiredArgsConstructor
@Repository
public class BoardArticleCountRepository {
    private final StringRedisTemplate redisTemplate;


    private static final String KEY_FORMAT ="article-read::board-article-count::board::%s";


    public void createOrUpdate(Long boardId, Long articleCount)
    {
        redisTemplate.opsForValue().set(generateKry(boardId), String.valueOf(articleCount));
    }

    public Long read(Long boardId)
    {
        String result = redisTemplate.opsForValue().get(generateKry(boardId));
        return result == null ? 0L : Long.valueOf(result);
    }

    private String generateKry(Long boardId)
    {
        return KEY_FORMAT.formatted(boardId);
    }
}
