package dding.board.board.PrimaryKeyProvider;

import dding.board.board.snowflake.Snowflake;

public class PrimaryIdProvider {
    private final Snowflake snowflake = new Snowflake();


    public  long getId()
    {
        return snowflake.nextId();
    }
}
