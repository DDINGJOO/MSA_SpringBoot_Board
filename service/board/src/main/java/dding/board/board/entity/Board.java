package dding.board.board.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Table(name = "board")
@Entity
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Board {
    @Id
    private Long boardId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long writerId;

    public static Board create(Long boardId, String title, Long writerId){
        Board board = new Board();
        board.boardId = boardId;
        board.title = title;
        board.writerId =writerId;
        board.createdAt = LocalDateTime.now();
        board.modifiedAt = LocalDateTime.now();
        return board;
    }
}
