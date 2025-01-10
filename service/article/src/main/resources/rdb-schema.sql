create table article(
                        article_id bigint not null primary key ,
                        title varchar(100) not null ,
                        content varchar(300) not null ,
                        board_id bigint not null ,
                        created_at datetime not null ,
                        modified_ad datetime not null
);