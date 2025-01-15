package dding.board.like.util.PKProvider;

import dding.board.common.snowflake.Snowflake;
import dding.board.like.service.PKProvicer;

public class SnowFlakePKProvider implements PKProvicer {
    private final Snowflake snowflake = new Snowflake();
    @Override
    public Long getId() {
        return  snowflake.nextId();
    }


}
