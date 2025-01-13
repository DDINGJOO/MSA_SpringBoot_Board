package dding.board.article.repository;

import dding.board.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Long> {


    @Query(
            value = "select article.article_id, article.title, article.content, article.board_id, article.writer_id, " +
                    "article.created_at, article.modified_at " +
                    "from (" +
                    "   select article_id from article " +
                    "   where board_id = :boardId " +
                    "   order by article_id desc " +
                    "   limit :limit offset :offset " +
                    ") t left join article on t.article_id = article.article_id ",
            nativeQuery = true
    )
    List<Article> findAll(
            @Param("boardId") Long boardId,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );



    @Query(
            value = "SELECT article.article_id,article.title,article.wrtier_id" +
                    "FROM article" +
                    "WHERE board_id = 1" +
                    "  AND article_id < :article_id" +
                    "ORDER BY article_id DESC" +
                    "limit :limit",
            nativeQuery = true
    )
    List<Article> nearArticle(
            @Param("articl_id") Long article_id,
            @Param("limit") Long limit
    );



    @Query(
            value = "select  count(*) from(" +
                    "select article_id from article where board_id = :boardId limit :limit" +
                    ")t",
            nativeQuery = true

    )
    Long count(@Param("boardId") Long boardId, @Param("limit") Long limit);




}
