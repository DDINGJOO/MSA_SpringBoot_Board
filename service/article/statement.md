## 게시글 테이블 설계


- article_id /BIGINT / Primary Key
- title      /VARCHAR(100) / 제목
- content    /VARCHAR(300) / 내용
- board_id   /BIGINT       / 게시판ID(Shard Key)
- writer_id  /BIGINT       / 작성자 ID
- created_at / DATETIME / 
- modified_at / DATETIME / 




- N개의 샤드로 분산되는 상황까지만 고려
- 샤드 키 -> 게시판 ID :게시판 단위로 조회가 되기에 가장 합리적임



```SQL
create table article(
    article_id bigint not null primary key ,
	title varchar(100) not null ,
	content varchar(300) not null ,
	board_id bigint not null ,
	created_at datetime not null ,
	modified_ad datetime not null 
);


```