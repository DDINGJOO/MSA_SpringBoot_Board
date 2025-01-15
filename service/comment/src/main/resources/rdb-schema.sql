CREATE TABLE comment (
                         comment_id BIGINT NOT NULL PRIMARY KEY,
                         content VARCHAR(3000) NOT NULL,
                         article_id BIGINT NOT NULL,
                         parent_comment_id BIGINT NOT NULL,
                         writer_id BIGINT NOT NULL,
                         deleted BOOLEAN NOT NULL,
                         created_at DATETIME NOT NULL
);
