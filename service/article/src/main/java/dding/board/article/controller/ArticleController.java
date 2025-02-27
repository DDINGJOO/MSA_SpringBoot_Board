package dding.board.article.controller;


import dding.board.article.service.ArticleService;
import dding.board.article.dto.request.ArticleCreateRequest;
import dding.board.article.dto.request.ArticleUpdateRequest;
import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private  final ArticleService articleService;


    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse read(
            @PathVariable("articleId") Long articleId
    )
    {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse readAll(
            @RequestParam("boardId") Long boardId,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam("page") Long page
    ){
        
        return articleService.readAll(boardId,page,pageSize);
    }

    @GetMapping("/v1/articles/infinite-Scroll")
    public List<ArticleResponse> readAllInfiniteScroll(
            @RequestParam("boardId") Long boardId,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam(value = "lastArticleId", required = false) Long lastArticleId
    )
    {
        return articleService.readAllInfiniteScroll(boardId,pageSize,lastArticleId);
    }

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }



    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable("articleId") Long articleId, @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable("articleId")Long articleId) {
        articleService.delete(articleId);
    }


    @GetMapping("/v1/articles/boards/{boardId}/count")
    public Long count(@PathVariable("boardId") Long boardId)
    {
        return articleService.count(boardId);
    }

}
