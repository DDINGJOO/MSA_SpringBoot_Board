package dding.board.comment.service;


import dding.board.comment.dto.request.CommentCreateRequest;
import dding.board.comment.dto.response.CommentResponse;
import dding.board.comment.entity.Comment;
import dding.board.comment.repository.CommentRepository;
import dding.board.comment.util.PKProvider.SnowFlakePKProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PKProvider pkProvider = new SnowFlakePKProvider();



    @Transactional
    public CommentResponse create (CommentCreateRequest req)
    {
        Comment parent = findParent(req);
        Comment comment = commentRepository.save(
                Comment.create(
                        pkProvider.getId(),
                        req.getContent(),
                        req.getParentCommentId(),
                        req.getArticleId(),
                        req.getWriterId()
                )
        );
        return CommentResponse.from(comment);
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
    public CommentResponse read (Long commentId)
    {
        return CommentResponse.from(
                commentRepository.findById(commentId).orElseThrow()
       );
    }


    @Transactional
    public void delete(Long commentId)
    {
        commentRepository.delete(commentRepository.findById(commentId).orElseThrow());
    }

}
