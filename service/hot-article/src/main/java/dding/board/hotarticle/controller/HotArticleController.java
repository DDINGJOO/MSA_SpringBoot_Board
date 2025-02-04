package dding.board.hotarticle.controller;


import dding.board.hotarticle.dto.response.HotArticleResponse;
import dding.board.hotarticle.service.HotArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotArticleController {
    private final HotArticleService hotArticleService;


    @GetMapping("/v1/hot-articles/articles/date/{dataStr}")
    //yyyyMMdd
    public List<HotArticleResponse> readAll(
            @PathVariable("dataStr") String dataStr
    )
    {
        return hotArticleService.readAll(dataStr);
    }
}
