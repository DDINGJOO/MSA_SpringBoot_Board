package dding.board.like.service;

import dding.board.like.dto.Response.ArticleLikeResponse;
import dding.board.like.entity.ArticleLike;
import dding.board.like.repository.ArticleLikeRepository;
import dding.board.like.util.PKProvider.SnowFlakePKProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final PKProvicer pkProvicer = new SnowFlakePKProvider();
    private final ArticleLikeRepository articleLikeRepository;




    ArticleLikeResponse read(Long articleId, Long userId)
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
                        pkProvicer.getId(),
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

}
