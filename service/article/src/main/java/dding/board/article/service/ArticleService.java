package dding.board.article.service;


import dding.board.article.entity.Article;
import dding.board.article.exceptions.NotFoundArticleById;
import dding.board.article.repository.ArticleRepository;
import dding.board.article.service.request.ArticleCreateRequest;
import dding.board.article.service.request.ArticleUpdateRequest;
import dding.board.article.service.response.ArticlePageResponse;
import dding.board.article.service.response.ArticleResponse;
import dding.board.common.snowflake.Snowflake;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;



    @Transactional
    public ArticleResponse create(ArticleCreateRequest request)
    {
        Article article = articleRepository.save(
                Article.create(
                        snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );

        return ArticleResponse.form(article);
    }



    @Transactional
    public ArticleResponse update(Long articleId,ArticleUpdateRequest req)
    {
       Article article = articleRepository.findById(articleId).orElseThrow((NotFoundArticleById::new));
       article.update(req.getTitle(), req.getContent());
       return ArticleResponse.form(article);
    }


    public ArticleResponse read( Long articleId)
    {
        return ArticleResponse.form(articleRepository.findById(articleId).orElseThrow(NotFoundArticleById::new));
    }



    @Transactional
    public void delete(Long articleId)
    {
        articleRepository.deleteById(articleId);
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize)
    {
        return ArticlePageResponse.of(
                articleRepository.findAll(boardId, (page -1) * pageSize, pageSize).stream()
                        .map(ArticleResponse::form)
                        .toList(),
                articleRepository.count(
                        boardId,
                        PageLimitCalculator.calculator(page,pageSize,10L)
                )
        );
    }

}
