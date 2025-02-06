package dding.board.articleread.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dding.board.common.dataserializer.DataSerializer;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Getter

public class OptimizedCache {
    private String data;
    private LocalDateTime expiredAt;

    public static OptimizedCache of(Object data, Duration ttl)
    {
        OptimizedCache optimizedCache = new OptimizedCache();
        optimizedCache.data = DataSerializer.serialize(data);
        optimizedCache.expiredAt = LocalDateTime.now().plus(ttl);
        return optimizedCache;
    }


    @JsonIgnore
    public boolean isExpired()
    {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public <T> T parseDate(Class<T> dataType)
    {
        return  DataSerializer.deserialize(data,dataType);
    }
}
