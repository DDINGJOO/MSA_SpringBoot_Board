package dding.board.article.exceptions;

public class NotFoundArticleById extends RuntimeException {
    public NotFoundArticleById() {
        super("지정된 articleId를 갖는 게시물을 찾을 수 없습니다.");
    }
}
