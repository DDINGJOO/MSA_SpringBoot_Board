package dding.board.article.service;


import dding.board.article.entity.Article;
import dding.board.article.entity.BoardArticleCount;
import dding.board.article.exceptions.NotFoundArticleById;
import dding.board.article.repository.ArticleRepository;
import dding.board.article.dto.request.ArticleCreateRequest;
import dding.board.article.dto.request.ArticleUpdateRequest;
import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import dding.board.article.repository.BoardArticleCountRepository;
import dding.board.common.PrimaryKeyProvider.PrimaryIdProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final PrimaryIdProvider primaryIdProvider;
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;


    @Transactional
    public ArticleResponse create(ArticleCreateRequest request)
    {
        Article article = articleRepository.save(
                Article.create(
                        primaryIdProvider.getId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );

        int result = boardArticleCountRepository.increase(request.getBoardId());
        if(result == 0 )
        {
            boardArticleCountRepository.save(
                    BoardArticleCount.init(request.getBoardId())
            );
        }
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




    public List<ArticleResponse> readAllInfiniteScroll(Long boardId , Long pageSize, Long lastArticleId)
    {
        List<Article> articles = lastArticleId == null?
                articleRepository.findAllInfiniteScroll(boardId,pageSize):
                articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
        return articles.stream()
                .map(ArticleResponse :: form)
                .toList();
    }




    @Transactional
    public void delete(Long articleId)
    {
        Article article = articleRepository.findById(articleId).orElseThrow();
        boardArticleCountRepository.decrease(article.getBoardId());
        articleRepository.delete(article);

    }


    public Long count(Long boardId)
    {
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount)
                .orElse(0L);
    }

}
