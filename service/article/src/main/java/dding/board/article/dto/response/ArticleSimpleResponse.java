package dding.board.article.dto.response;

import com.sun.jdi.LongValue;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ArticleSimpleResponse {
    private List<ArticleResponse> articles;
    private Long articleCount;


    public static ArticleSimpleResponse of( List<ArticleResponse> articles, Long articleCount)
    {
        var response = new ArticleSimpleResponse();
        response.articles = articles;
        response.articleCount = articleCount;
        return response;
    }
}
