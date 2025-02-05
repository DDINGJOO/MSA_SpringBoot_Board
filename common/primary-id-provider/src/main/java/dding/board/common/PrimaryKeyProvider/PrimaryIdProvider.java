package dding.board.common.PrimaryKeyProvider;

import dding.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrimaryIdProvider {
    private final Snowflake snowflake;


    public  long getId()
    {
        return snowflake.nextId();
    }
}
