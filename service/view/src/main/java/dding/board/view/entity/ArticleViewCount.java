package dding.board.view.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name = "article_view_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleViewCount {

    @Id
    private Long articleId; // shardKey
    private Long viewCount;



    public static ArticleViewCount init (Long articleId)
    {
        var result = new ArticleViewCount();
        result.articleId = articleId;
        result.viewCount = 0L;
        return  result;
    }

}
