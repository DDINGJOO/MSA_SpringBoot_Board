create table commnet (
    comment_id bigint not null primary key ,
    content varchar(3000) not null ,
    article_id bigint not null ,
    parent_comment_id bigint not null ,
    writer_id bigint not null,
    deleted bool not null ,
    created_at datetime not null
);