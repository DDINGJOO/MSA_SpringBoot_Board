package dding.board.like.controller;


import dding.board.like.dto.Response.ArticleLikeResponse;
import dding.board.like.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;


    @GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public ArticleLikeResponse read(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        return articleLikeService.read(articleId,userId);
    }

    /**
     *NotUsed
     */
    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public void like(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.like(articleId,userId);
    }

    /**
     *Not Used
     */
    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public void unlike(
        @PathVariable("articleId") Long articleId,
        @PathVariable("userId") Long userId
    )
    {
        articleLikeService.unLike(articleId,userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void likePessimisticLock1(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.likePessimisticLock1(articleId,userId);
    }
    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void unlikePessimisticLock1(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.unLikePessimisticLock1(articleId,userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void likePessimisticLock2(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.likePessimisticLock2(articleId,userId);
    }
    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void unlikePessimisticLock2(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.unlikePessimisticLock2(articleId,userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock-1")
    public void likeOptimisticLock(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.likeOptimisticLock(articleId,userId);
    }
    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock-1")
    public void unlikeOptimisticLock(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    )
    {
        articleLikeService.unlikeOptimisticLock(articleId,userId);
    }




}
