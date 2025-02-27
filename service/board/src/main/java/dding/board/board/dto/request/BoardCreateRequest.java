package dding.board.board.dto.request;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardCreateRequest {
    private String title;
    private Long parentBoardId;
    private Long writerId;


}
