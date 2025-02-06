package dding.board.board.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Table(name = "board")
@Entity
@Getter
@RequiredArgsConstructor
public class Board {
    @Id
    private Long boardId;
    
}
