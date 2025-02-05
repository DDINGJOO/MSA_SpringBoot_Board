package dding.board.comment.service;


import dding.board.comment.dto.request.CommentCreateRequest;
import dding.board.comment.dto.response.CommentPageResponse;
import dding.board.comment.dto.response.CommentResponse;
import dding.board.comment.entity.ArticleCommentCount;
import dding.board.comment.entity.Comment;
import dding.board.comment.repository.ArticleCommentCountRepository;
import dding.board.comment.repository.CommentRepository;
import dding.board.common.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleViewedEventPayload;
import dding.board.common.event.payload.CommentCreatedEventPayload;
import dding.board.common.outboxmessagerelay.OutboxEventPublisher;
import dding.board.common.outboxmessagerelay.OutboxRepository;
import exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final PrimaryIdProvider primaryKeyProvider = new PrimaryIdProvider();
    private final OutboxRepository outboxRepository;


    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        Comment parent = findParent(request);
        Comment comment = commentRepository.save(
                Comment.create(
                        primaryKeyProvider.getId(),
                        request.getContent(),
                        parent == null ? null : parent.getCommentId(),
                        request.getArticleId(),
                        request.getWriterId()
                )

        );
        var result = articleCommentCountRepository.increase(request.getArticleId());
        if(result == 0)
        {
            articleCommentCountRepository.save(
                    ArticleCommentCount.init(request.getArticleId())
            );
        }

        outboxEventPublisher.publish(
                EventType.COMMENT_CREATED,
                CommentCreatedEventPayload.builder()
                        .createdAt(comment.getCreatedAt())
                        .articleCommentCount(count(comment.getArticleId()))
                        .commentId(comment.getCommentId())
                        .articleId(comment.getArticleId())
                        .content(comment.getContent())
                        .deleted(comment.getDeleted())
                        .writerId(comment.getWriterId())
                        .build(),
                comment.getArticleId()
        );


        return CommentResponse.from(comment);
    }

    public CommentResponse read (Long commentId)
    {
        return CommentResponse.from(
                commentRepository.findById(commentId).orElseThrow(()
                        -> new NotFoundException("지정된 오브젝트를 찾을 수 없습니다. code:404"))
        );
    }


    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize) {
        return CommentPageResponse.of(
                commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from)
                        .toList(),
                commentRepository.count(articleId, PageLimitCalculator.calculator(page, pageSize, 10L))
        );
    }


    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    outboxEventPublisher.publish(
                            EventType.COMMENT_CREATED,
                            CommentCreatedEventPayload.builder()
                                    .createdAt(comment.getCreatedAt())
                                    .articleCommentCount(count(comment.getArticleId()))
                                    .commentId(comment.getCommentId())
                                    .articleId(comment.getArticleId())
                                    .content(comment.getContent())
                                    .deleted(comment.getDeleted())
                                    .writerId(comment.getWriterId())
                                    .build(),
                            comment.getArticleId()
                    );
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }

                });


    }

    private Comment findParent(CommentCreateRequest req)
    {
        var parentCommentId = req.getParentCommentId();
        if(parentCommentId == null)
        {
            return null;
        }
        return  commentRepository.findById(parentCommentId)
                .filter(not(Comment::getDeleted))
                .filter(Comment::isRoot)
                .orElseThrow();
    }







    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    @Transactional
    protected void delete(Comment comment) {
        articleCommentCountRepository.decrease(comment.getArticleId());
        commentRepository.delete(comment);
        if (!comment.isRoot()) {
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }



    public List<CommentResponse> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit) {
        List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
                commentRepository.findAllInfiniteScroll(articleId, limit) :
                commentRepository.findAllInfiniteScroll(articleId, lastParentCommentId, lastCommentId, limit);
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    public Long count(Long articleId)
    {
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }


}
