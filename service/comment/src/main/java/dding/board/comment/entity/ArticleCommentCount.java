package dding.board.comment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_comment_count")
@Getter
@ToString
@NoArgsConstructor
@Entity
public class ArticleCommentCount {
    @Id
    private Long articleId;
    private Long CommentCount;


    public static ArticleCommentCount init(Long articleId)
    {
        var response = new ArticleCommentCount();
        response.articleId = articleId;
        response.CommentCount = 1L;
        return  response;
    }
}
