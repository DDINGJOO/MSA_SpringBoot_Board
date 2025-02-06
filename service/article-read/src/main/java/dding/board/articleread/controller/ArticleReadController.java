package dding.board.articleread.controller;


import dding.board.articleread.dto.response.ArticleReadResponse;
import dding.board.articleread.service.ArticleReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleReadController {
    private final ArticleReadService articleReadService;


    @GetMapping("/v1/articles/{articleId}")
    public ArticleReadResponse read(
            @PathVariable("articleId") Long articleId
    )
    {
        return articleReadService.read(articleId);
    }
}
