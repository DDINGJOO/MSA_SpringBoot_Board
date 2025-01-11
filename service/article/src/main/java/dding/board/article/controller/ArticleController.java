package dding.board.article.controller;


import dding.board.article.service.ArticleService;
import dding.board.article.service.request.ArticleCreateRequest;
import dding.board.article.service.request.ArticleUpdateRequest;
import dding.board.article.service.response.ArticlePageResponse;
import dding.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private  final ArticleService articleService;

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse reade(@PathVariable Long articleId)
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

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }
}
