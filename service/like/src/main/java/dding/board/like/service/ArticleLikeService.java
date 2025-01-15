package dding.board.like.service;

import dding.board.like.dto.Response.ArticleLikeResponse;
import dding.board.like.entity.ArticleLike;
import dding.board.like.entity.ArticleLikeCount;
import dding.board.like.repository.ArticleLikeCountRepository;
import dding.board.like.repository.ArticleLikeRepository;
import dding.board.like.util.PKProvider.SnowFlakePKProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final PKProvider pkProvider = new SnowFlakePKProvider();
    private final ArticleLikeRepository articleLikeRepository;
    private  final ArticleLikeCountRepository articleLikeCountRepository;


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
                        pkProvider.getId(),
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
        articleLikeRepository.save(
                ArticleLike.create(
                        pkProvider.getId(),
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


    }

    @Transactional
    public void unLikePessimisticLock1(Long articleId, Long userId)
    {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);
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
                        pkProvider.getId(),
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
                        pkProvider.getId(),
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


}
