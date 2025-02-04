package dding.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class HotArticleListRepository {
    private final StringRedisTemplate redisTemplate;


    //hot-article::list::{yyyyMMdd}
    private static final String KET_FORMAT = "hot-article::list::%s";

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");


    public void add(Long articleId, LocalDateTime time, Long score , Long limit, Duration ttl)
    {
        redisTemplate.executePipelined((RedisCallback<?>) action ->{
            StringRedisConnection conn  = (StringRedisConnection) action;
            String key = generateKey(time);
            conn.zAdd(key,score,String.valueOf(articleId));
            conn.zRemRange(key, 0,-limit-1);
            conn.expire(key,ttl.toSeconds());
            return null;
        });
    }

    public List<Long> readAll(String date)
    {
        return redisTemplate.opsForZSet()
                .reverseRangeWithScores(generateKey(date),0, -1).stream()
                .peek(tuple -> log.info(
                        "[HotArticleListRepository.readAll] articleId = {}, score = {}", tuple.getValue(), tuple.getScore()))
                .map(ZSetOperations.TypedTuple::getValue)
                .map(Long::valueOf)
                .toList();

    }

    public void remove(Long articleId, LocalDateTime time)
    {
        redisTemplate.opsForZSet().remove(generateKey(time), String.valueOf(articleId));
    }


    private String generateKey(LocalDateTime time){
        return generateKey(TIME_FORMAT.format(time));
    }

    private String generateKey(String datestr)
    {
        return KET_FORMAT.formatted(datestr);
    }
}
