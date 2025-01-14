package dding.board.comment.service;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class PageLimitCalculator {

    public static Long calculator(Long page, Long pageSize, Long movablePageCount)
    {
        return (((page -1)/movablePageCount) +1) * pageSize * movablePageCount +1;
    }
}
