package dding.board.board.dto.response;


import dding.board.board.entity.Board;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

public class BoardResponse {
    private Long boardId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long writerId;

    public static BoardResponse from(Board board){
        BoardResponse boardResponse = new BoardResponse();
        boardResponse.boardId = board.getBoardId();
        boardResponse.title = board.getTitle();
        boardResponse.writerId =board.getWriterId();
        boardResponse.createdAt = board.getCreatedAt();
        boardResponse.modifiedAt = board.getModifiedAt();
        return boardResponse;
    }
}