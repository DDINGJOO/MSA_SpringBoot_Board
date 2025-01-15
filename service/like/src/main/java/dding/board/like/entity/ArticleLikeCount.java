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
    private Long article_id; //shardKey
    private Long like_count;

    @Version
    private Long version;


    public static ArticleLikeCount create (Long article_id, Long like_count)
    {
        ArticleLikeCount articleLikeCount = new ArticleLikeCount();
        articleLikeCount.article_id = article_id;
        articleLikeCount.like_count = like_count;
        articleLikeCount.version = 0L;
        return articleLikeCount;
    }


    public void increase()
    {
        this.like_count  +=1;
    }
    public void decrease()
    {
        this.like_count -=1;
    }
}
