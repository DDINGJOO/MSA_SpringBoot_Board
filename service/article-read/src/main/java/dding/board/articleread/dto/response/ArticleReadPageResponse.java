package dding.board.articleread.dto.response;


import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString

public class ArticleReadPageResponse {
    private List<ArticleReadResponse> articles;
    private Long articleCount;

    public static ArticleReadPageResponse of(List<ArticleReadResponse> articles,Long articleCount)
    {
        ArticleReadPageResponse response = new ArticleReadPageResponse();
        response.articles = articles;
        response.articleCount = articleCount;
        return response;
    }
}
