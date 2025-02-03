package dding.board.article.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board_article_count")
@ToString
public class BoardArticleCount
{
    @Id
    private Long boardId;  //shardKey

    private Long articleCount;



    public static BoardArticleCount init(Long boardId)
    {
        var boardArticleCount = new BoardArticleCount();
        boardArticleCount.boardId = boardId;
        boardArticleCount.articleCount = 1L;
        return boardArticleCount;
    }
}
