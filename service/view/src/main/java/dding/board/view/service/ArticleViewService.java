package dding.board.view.service;


import dding.board.view.repository.ArticleViewCountRepository;
import dding.board.view.repository.ArticleViewDistributedLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleVIewCountBackUpProcessor articleVIewCountBackUpProcessor;
    private  final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private static final int BACK_UP_BACH_SIZE  = 100;
    private static final Duration TTL = Duration.ofMinutes(10);

    public Long increase(Long articleId, Long userId)
    {
        if(!articleViewDistributedLockRepository.lock(articleId,userId,TTL))
        {
            return articleViewCountRepository.read(articleId);
        }

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
