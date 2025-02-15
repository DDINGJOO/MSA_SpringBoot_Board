package dding.board.like.service;

import dding.board.board.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.board.event.EventType;
import dding.board.board.event.payload.ArticleLikedEventPayload;
import dding.board.board.event.payload.ArticleUnlikedEventPayload;
import dding.board.board.outboxmessagerelay.OutboxEventPublisher;
import dding.board.like.dto.Response.ArticleLikeResponse;
import dding.board.like.entity.ArticleLike;
import dding.board.like.entity.ArticleLikeCount;
import dding.board.like.repository.ArticleLikeCountRepository;
import dding.board.like.repository.ArticleLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final PrimaryIdProvider primaryIdProvider = new PrimaryIdProvider();
    private final ArticleLikeRepository articleLikeRepository;
    private  final ArticleLikeCountRepository articleLikeCountRepository;
    private final OutboxEventPublisher outboxEventPublisher;


    public ArticleLikeResponse read(Long articleId, Long userId)
    {
        
        return  articleLikeRepository.findByArticleIdAndUserId(articleId,userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }



    @Transactional
    public void like(Long articleId, Long userId)
    {
        articleLikeRepository.save(
                ArticleLike.create(
                        primaryIdProvider.getId(),
                        articleId,
                        userId
                )
        );

    }

    @Transactional
    public void unLike(Long articleId, Long userId)
    {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLikeRepository::delete);
    }


    /**
     * update 구문 날리기
     */
    @Transactional
    public void likePessimisticLock1(Long articleId, Long userId)
    {
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(
                        primaryIdProvider.getId(),
                        articleId,
                        userId
            )
        );
        int result = articleLikeCountRepository.increase(articleId);
        if(result ==0)
        {
            //최초 요청시 -> 게시글 최초 생성시 그냥 0 으로 설정하는 방법이..?
            articleLikeCountRepository.save(
                    ArticleLikeCount.create(articleId,1L)

            );
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_LIKED,
                ArticleLikedEventPayload.builder()
                        .articleLikeId(articleLike.getArticleLikeId())
                        .articleId(articleLike.getArticleId())
                        .articleLikeCount(count(articleId))
                        .createdAt(articleLike.getCreatedAt())
                        .userId(articleLike.getUserId())
                        .build(),
                articleLike.getArticleId()//shardKey
        );


    }

    @Transactional
    public void unLikePessimisticLock1(Long articleId, Long userId)
    {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);
                    outboxEventPublisher.publish(
                            EventType.ARTICLE_UNLIKED,
                            ArticleUnlikedEventPayload.builder()
                                    .articleLikeId(articleLike.getArticleLikeId())
                                    .articleId(articleLike.getArticleId())
                                    .articleLikeCount(count(articleId))
                                    .createdAt(articleLike.getCreatedAt())
                                    .userId(articleLike.getUserId())
                                    .build(),
                            articleLike.getArticleId()//shardKey
                    );
                } );


    }

    /**
     * select ... for update ...
     */
    @Transactional
    public void likePessimisticLock2(Long articleId, Long userId)
    {
        articleLikeRepository.save(
                ArticleLike.create(
                        primaryIdProvider.getId(),
                        articleId,
                        userId
                )
        );

        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
                .orElseGet(()-> ArticleLikeCount.create(articleId,0L));
        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount);
    }


    @Transactional
    public void unlikePessimisticLock2(Long articleId, Long userId)
    {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow();
                    articleLikeCount.decrease();;
                } );

    }

    /**
     * optimistic Lock
     */
    @Transactional
    public void likeOptimisticLock(Long articleId, Long userId)
    {
        articleLikeRepository.save(
                ArticleLike.create(
                        primaryIdProvider.getId(),
                        articleId,
                        userId
                )
        );
        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId).orElseGet(() -> ArticleLikeCount.create(articleId, 0L));
        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount);
    }


    @Transactional
    public void unlikeOptimisticLock(Long articleId, Long userId)
    {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId).orElseThrow();
                    articleLikeCount.decrease();
                });
    }


    public Long count(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);
    }
}
