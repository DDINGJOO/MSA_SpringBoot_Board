package dding.board.articleread.cache;


import dding.board.board.dataserializer.DataSerializer;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.stream.Collectors;

@CommandNaming
@RequiredArgsConstructor
public class OptimizedCacheManager {
    private final StringRedisTemplate redisTemplate;

    private final OptimizedCacheLockProvider optimizedCacheLockProvider;


    private static final String DELIMITER = "::";

    public Object process(String type ,long ttlSeconds, Object[] args, Class<?> returnType
    ,OptimizedCacheOriginDataSupplier<?> originDataSupplier)throws Throwable
    {
        String key = generateKey(type, args);

        String cachedDate = redisTemplate.opsForValue().get(key);
        if(cachedDate ==null)
        {
            return refresh(originDataSupplier, key ,ttlSeconds);
        }


        OptimizedCache optimizedCache = DataSerializer.deserialize(cachedDate, OptimizedCache.class);
        if(optimizedCache == null)
        {
            return refresh(originDataSupplier, key, ttlSeconds);
        }
        if(!optimizedCache.isExpired())
        {
            return optimizedCache.parseDate(returnType);
        }
        if(!optimizedCacheLockProvider.lock(key)){
            return optimizedCache.parseDate(returnType);
        }
        try{
            return refresh(originDataSupplier, key, ttlSeconds);
        } finally {
            optimizedCacheLockProvider.unLock(key);
        }
    }

    private Object refresh(OptimizedCacheOriginDataSupplier<?> originDataSupplier,
                           String key, long ttlSeconds) throws Throwable
    {
        Object result = originDataSupplier.get();
        OptimizedCacheTTL optimizedCacheTTL = OptimizedCacheTTL.of(ttlSeconds);
        OptimizedCache optimizedCache = OptimizedCache.of(result,optimizedCacheTTL.getLogicalTTL());

        redisTemplate.opsForValue().set(
                key,
                DataSerializer.serialize(optimizedCache),
                optimizedCacheTTL.getPhysicalTTL());


        return result;
    }


    public String generateKey(String prefix, Object[] args)
    {
        return prefix  + DELIMITER +
                Arrays.stream(args)
                        .map(String::valueOf)
                        .collect(Collectors.joining(DELIMITER));
    }
}


