CREATE TABLE article_like (
    article_like_id bigint not null primary key ,
    article_id bigint not null ,
    user_id bigint not null ,
    created_at datetime not null
);
create unique index idx_article_id_user_id on article_like(article_id asc, user_id asc);