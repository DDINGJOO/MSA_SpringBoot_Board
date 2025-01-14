package dding.board.comment.repository;

import dding.board.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<Comment,Long> {


    @Query(
            value = "select count(*) from (" +
                    "select comment_id from comment " +
                    "where article_id = :articleId and parent_comment_id = :parentCommentId "+
                    "limit :limit" +
                    ") t",
            nativeQuery = true
    )

    Long countBy(
            @Param("article_id") Long articleId,
            @Param("parent_comment_id") Long parentCommentId,
            @Param("limit") Long limit
    );

}
