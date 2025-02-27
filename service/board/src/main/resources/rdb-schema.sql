create table IF NOT EXISTS board (
                         board_id bigint not null primary key,
                         title varchar(100) not null,
                         writer_id bigint not null,
                         created_at datetime not null,
                         parent_board_id BIGINT NOT NULL,
                         deleted BOOLEAN NOT NULL,
                         modified_at datetime not null
);

