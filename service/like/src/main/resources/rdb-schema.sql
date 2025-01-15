CREATE TABLE article-like (
    article_like_id bigint not null primary key ,
    article_id bigint not null ,
    user_id bigint not null ,
    created_at datetime not null
);
