package dding.board.board.controller;

import dding.board.board.dto.request.BoardCreateRequest;
import dding.board.board.dto.response.BoardResponse;
import dding.board.board.entity.Board;
import dding.board.board.service.BoardService;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;


@RestController
public class BoardController {

    BoardService boardService;

    @PostMapping("/v1/boards")
    @Transactional
    public BoardResponse create (@RequestBody BoardCreateRequest req)
    {
        return boardService.create(req);
    }

    @GetMapping("/v1/boards/{boardId}")
    public BoardResponse read(@PathVariable("boardId") Long boardId )
    {
        return boardService.read(boardId);
    }

    @DeleteMapping("/v1/boards/{boardId")
    public void delete(@PathParam("boardId") Long boardId)
    {
        boardService.delete(boardId);
    }




}
