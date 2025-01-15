package dding.board.like.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;

@Table(name ="article_like_count")
@Entity
@ToString
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class ArticleLikeCount {
    @Id
    private Long articleId; //shardKey
    private Long likeCount;

    @Version
    private Long version;


    public static ArticleLikeCount create (Long articleId, Long likeCount)
    {
        ArticleLikeCount articleLikeCount = new ArticleLikeCount();
        articleLikeCount.articleId = articleId;
        articleLikeCount.likeCount = likeCount;
        articleLikeCount.version = 0L;
        return articleLikeCount;
    }


    public void increase()
    {
        this.likeCount  +=1;
    }
    public void decrease()
    {
        this.likeCount  -=1;
    }
}
