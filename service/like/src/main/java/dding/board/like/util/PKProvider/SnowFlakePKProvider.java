package dding.board.like.util.PKProvider;

import dding.board.common.snowflake.Snowflake;
import dding.board.like.service.PKProvider;

public class SnowFlakePKProvider implements PKProvider {
    private final Snowflake snowflake = new Snowflake();
    @Override
    public Long getId() {
        return  snowflake.nextId();
    }


}
