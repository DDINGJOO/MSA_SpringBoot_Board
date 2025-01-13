package dding.board.article.util.PrimaryKeyProvider;

import dding.board.article.service.PrimaryKeyProvider;
import dding.board.common.snowflake.Snowflake;

public class SnowFlakeKeyProvider implements PrimaryKeyProvider {
    private final Snowflake snowflake = new Snowflake();
    @Override
    public Long getId() {
        return snowflake.nextId();
    }
}
