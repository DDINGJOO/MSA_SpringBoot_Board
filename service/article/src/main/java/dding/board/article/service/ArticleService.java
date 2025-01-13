package dding.board.article.service;


import dding.board.article.entity.Article;
import dding.board.article.exceptions.NotFoundArticleById;
import dding.board.article.repository.ArticleRepository;
import dding.board.article.dto.request.ArticleCreateRequest;
import dding.board.article.dto.request.ArticleUpdateRequest;
import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import dding.board.article.util.PrimaryKeyProvider.SnowFlakeKeyProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final PrimaryKeyProvider primaryKeyProvider = new SnowFlakeKeyProvider();
    private final ArticleRepository articleRepository;



    @Transactional
    public ArticleResponse create(ArticleCreateRequest request)
    {
        Article article = articleRepository.save(
                Article.create(
                        primaryKeyProvider.getId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
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




    @Transactional
    public void delete(Long articleId)
    {
        articleRepository.deleteById(articleId);
    }

}
