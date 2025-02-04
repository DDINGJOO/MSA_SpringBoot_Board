package dding.board.hotarticle.service;

import dding.board.common.event.Event;
import dding.board.hotarticle.repository.ArticleCreateTimeRepository;
import dding.board.hotarticle.repository.HotArticleListRepository;
import dding.board.hotarticle.service.eventHandler.EventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HotArticleScoreUpdaterTest {

    @InjectMocks
    HotArticleScoreUpdater hotArticleScoreUpdater;

    @Mock
    HotArticleListRepository hotArticleListRepository;
    @Mock
    HotArticleScoreCalculator hotArticleScoreCalculator;
    @Mock
    ArticleCreateTimeRepository articleCreateTimeRepository;



    @Test
    void updateIfArticleNotCreatedTodayTest()
    {
        //given
        Long articleId = 1L;
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);

        given(eventHandler.findArticleId(event)).willReturn(articleId);
        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);

        given(articleCreateTimeRepository.read(articleId)).willReturn(createdTime);


        //when
        hotArticleScoreUpdater.update(event,eventHandler);


        //then
        verify(eventHandler, never()).handle(event);
        verify(hotArticleListRepository, never())
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }

    @Test
    void updateIfArticleCreatedTodayTest()
    {
        //given
        Long articleId = 1L;
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);

        given(eventHandler.findArticleId(event)).willReturn(articleId);
        LocalDateTime createdTime = LocalDateTime.now();

        given(articleCreateTimeRepository.read(articleId)).willReturn(createdTime);


        //when
        hotArticleScoreUpdater.update(event,eventHandler);


        //then
        verify(eventHandler).handle(event);
        verify(hotArticleListRepository)
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }
}