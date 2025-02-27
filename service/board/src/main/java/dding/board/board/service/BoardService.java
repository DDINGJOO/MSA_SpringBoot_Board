package dding.board.board.service;

import dding.board.board.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.board.dto.request.BoardCreateRequest;
import dding.board.board.dto.response.BoardResponse;
import dding.board.board.entity.Board;
import dding.board.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final PrimaryIdProvider primaryIdProvider = new PrimaryIdProvider();


    @Transactional
    public BoardResponse create(BoardCreateRequest req)
    {
        Board parent = findParent(req);
        Board board = boardRepository.save(
                Board.create(
                        primaryIdProvider.getId(),
                        parent == null ? null : parent.getBoardId(),
                        req.getTitle(),
                        req.getWriterId()
                )
        );
        return BoardResponse.from(board);
    }

    public BoardResponse read (Long boardId)
    {
        Board board = boardRepository.findById(boardId).orElseThrow();
        return BoardResponse.from(board);
    }

    public void delete(Long boardId)
    {
        boardRepository.deleteById(boardId);
    }


    private Board findParent(BoardCreateRequest req)
    {
        var parentBoardId = req.getParentBoardId();
        if(parentBoardId == null)
        {
            return null;
        }
        return  boardRepository.findById(parentBoardId)
                .filter(not(Board::getDeleted))
                .filter(Board::isRoot)
                .orElseThrow();
    }


}
