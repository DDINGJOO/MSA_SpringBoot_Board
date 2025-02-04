package dding.board.hotarticle.service;

import dding.board.hotarticle.repository.ArticleCommentCountRepository;
import dding.board.hotarticle.repository.ArticleLikeCountRepository;
import dding.board.hotarticle.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;

    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;


    public long calculate(long articleId)
    {
        var articleLikeCount = articleLikeCountRepository.read(articleId);
        var articleCommentCount = articleCommentCountRepository.read(articleId);
        var articleViewCount = articleViewCountRepository.read(articleId);

        return articleLikeCount*ARTICLE_LIKE_COUNT_WEIGHT
                + articleCommentCount*ARTICLE_COMMENT_COUNT_WEIGHT
                + articleViewCount *ARTICLE_VIEW_COUNT_WEIGHT;

    }

}
