package dding.board.view.service;


import dding.board.view.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleVIewCountBackUpProcessor articleVIewCountBackUpProcessor;
    private static final int BACK_UP_BACH_SIZE  = 100;

    public Long increase(Long articleId, Long userId)
    {
        Long count =  articleViewCountRepository.increase(articleId);
        if(count % BACK_UP_BACH_SIZE == 0)
        {
            articleVIewCountBackUpProcessor.backup(articleId,count);
        }
        return count;
    }



    public Long count(Long articleId)
    {
        return articleViewCountRepository.read(articleId);
    }
}
