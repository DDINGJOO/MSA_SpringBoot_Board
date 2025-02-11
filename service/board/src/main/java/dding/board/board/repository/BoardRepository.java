package dding.board.board.repository;


import dding.board.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findByName(String name);
    Board findByBoardId(Long boardId);
    Board findByTitle(String title);

    
}
