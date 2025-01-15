package dding.board.like.controller;


import dding.board.like.dto.Response.ArticleLikeResponse;
import dding.board.like.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;


    @GetMapping("/v1/article-likes/articles/{articleId}/user/{userId}")
    public ArticleLikeResponse read(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        return articleLikeService.read(articleId,userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/user/{userId}")
    public void like(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.like(articleId,userId);
    }
    @DeleteMapping("/v1/article-likes/articles/{articleId}/user/{userId}")
    public void unlike(
        @PathVariable("articleId") Long articleId,
        @PathVariable("userId") Long userId
    )
    {
        articleLikeService.unLike(articleId,userId);
    }


}
