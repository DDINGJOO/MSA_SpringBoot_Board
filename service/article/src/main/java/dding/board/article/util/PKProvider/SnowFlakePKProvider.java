package dding.board.article.util.PKProvider;

import dding.board.article.service.PKProvider;
import dding.board.common.snowflake.Snowflake;

public class SnowFlakePKProvider implements PKProvider {
    private final Snowflake snowflake = new Snowflake();
    @Override
    public Long getId() {
        return snowflake.nextId();
    }
}
