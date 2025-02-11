create table IF NOT EXISTS board (
                         board_id bigint not null primary key,
                         title varchar(100) not null,
                         created_at datetime not null
);

