package dding.board.view.service;


import dding.board.view.entity.ArticleViewCount;
import dding.board.view.repository.ArticleViewCountBackUpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleVIewCountBackUpProcessor {

    private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;


    @Transactional
    public void backup(Long articleId, Long viewCount)
    {
        int result = articleViewCountBackUpRepository.updateViewCount(articleId,viewCount);
        if(result == 0)
        {
            articleViewCountBackUpRepository.findById(articleId)
                    .ifPresentOrElse(ignored -> { },
                        () -> articleViewCountBackUpRepository.save(
                                ArticleViewCount.init(articleId)
                        )
                    );
        }


    }
}
