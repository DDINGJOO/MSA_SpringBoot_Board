package dding.board.articleread.repository;


import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleIdListRepository {
    private final StringRedisTemplate redisTemplate;

    private static String KEY_FORMAT = "article-read::board::%s::article-list";

    public void add(Long boardId, Long articleId, Long limit)
    {
        redisTemplate.executePipelined((RedisCallback<?>) action ->{
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = generateKey(boardId);
            conn.zAdd(key, 0 , toPaddedString(articleId));
            conn.zRemRange(key,0, -limit-1);
            return null;
    });
    }

    public void delete(Long boardId, Long articleId)
    {
        redisTemplate.opsForZSet().remove(generateKey(boardId), toPaddedString(articleId));
    }

    public List<Long> readAll(Long boardId, Long offset, Long limit)
    {
        return redisTemplate.opsForZSet()
                .reverseRange(generateKey(boardId), offset, offset+limit-1)
                .stream().map(Long::valueOf).toList();
    }


    public List<Long> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long limit)
    {
        return redisTemplate.opsForZSet().reverseRangeByLex(
                generateKey(boardId),
                lastArticleId == null?
                        Range.unbounded():
                        Range.leftUnbounded(Range.Bound.exclusive(toPaddedString(lastArticleId))),
                Limit.limit().count(limit.intValue())
        ).stream().map(Long::valueOf).toList();

    }

    private String toPaddedString(Long articleId)
    {
        return "%019d".formatted(articleId);
        //1234 -> 0000000000000001234
    }


    private String generateKey(Long boardId)
    {
        return KEY_FORMAT.formatted(boardId);
    }

}
