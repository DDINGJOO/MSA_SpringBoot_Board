package dding.board.common.PrimaryKeyProvider;

import dding.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;

public class PrimaryIdProvider {
    private final Snowflake snowflake = new Snowflake();


    public  long getId()
    {
        return snowflake.nextId();
    }
}
