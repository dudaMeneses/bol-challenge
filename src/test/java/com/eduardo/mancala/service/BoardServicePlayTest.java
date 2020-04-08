package com.eduardo.mancala.service;

import com.eduardo.mancala.helper.BoardHelper;
import com.eduardo.mancala.matcher.BoardMatcher;
import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import com.eduardo.mancala.service.exception.BoardNotFoundException;
import com.eduardo.mancala.service.exception.EmptyPitException;
import com.eduardo.mancala.service.exception.GameFinishedException;
import com.eduardo.mancala.service.exception.WrongPlayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BoardServicePlayTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Captor
    private ArgumentCaptor<Board> captor;

    @BeforeEach
    public void init(){
    }

    @Test
    public void whenBoardNotFound_shouldThrowBoardNotFoundException(){
        doReturn(Optional.empty()).when(boardRepository).findById(anyString());

        assertThrows(
                BoardNotFoundException.class,
                () -> boardService.play(PlayRequest.builder()
                    .id("1L")
                    .build()
                )
        );
    }

    @Test
    public void whenBoardFinished_shouldThrowGameFinishedException(){
        doReturn(Optional.of(BoardHelper.finished())).when(boardRepository).findById(anyString());

        assertThrows(
                GameFinishedException.class,
                () -> boardService.play(PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.TWO)
                        .build()
                )
        );
    }

    @Test
    public void whenWrongPlayerPlaying_shouldThrowWrongPlayException(){
        doReturn(Optional.of(BoardHelper.create(PlayerEnum.ONE))).when(boardRepository).findById(anyString());

        assertThrows(
                WrongPlayException.class,
                () -> boardService.play(PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.TWO)
                        .build()
                )
        );
    }

    @Test
    public void whenSelectedPitHasNoStones_shouldThrowEmptyPitException(){
        doReturn(Optional.of(BoardHelper.withEmptyPit(PlayerEnum.ONE, 1))).when(boardRepository).findById(anyString());

        assertThrows(
                EmptyPitException.class,
                () -> boardService.play(PlayRequest.builder()
                        .id("1L")
                        .pitIndex(1)
                        .player(PlayerEnum.ONE)
                        .build()
                )
        );
    }

    @Test
    public void whenPlayNotEndsInKallah_shouldChangeNextToSideTwo(){
        doReturn(Optional.of(BoardHelper.create(PlayerEnum.ONE))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.TWO)).when(boardRepository).save(captor.capture());

        boardService.play(
               PlayRequest.builder()
                       .id("1L")
                       .player(PlayerEnum.ONE)
                       .pitIndex(4)
                       .build()
        );

        assertThat(captor.getValue(), BoardMatcher.playerTwoNext());
    }

    @Test
    public void whenPlayEndsInKallah_shouldKeepSameNext(){
        doReturn(Optional.of(BoardHelper.create(PlayerEnum.ONE))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.ONE)).when(boardRepository).save(captor.capture());

        boardService.play(
                PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.ONE)
                        .pitIndex(0)
                        .build()
        );

        assertThat(captor.getValue(), BoardMatcher.playerOneNext());
    }

    @Test
    public void whenPlayerTwoPassesKallah_shouldDistributeInSideOne(){
        doReturn(Optional.of(BoardHelper.create(PlayerEnum.TWO))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.ONE)).when(boardRepository).save(captor.capture());

        boardService.play(
                PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.TWO)
                        .pitIndex(5)
                        .build()
        );

        assertThat(captor.getValue(), BoardMatcher.playerOneNext());
    }

    @Test
    public void whenLastStoneGoesToEmptyPlayerOnePit_shouldCaptureMirroredPitStones(){
        doReturn(Optional.of(BoardHelper.withEmptyPit(PlayerEnum.ONE, 2))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.TWO)).when(boardRepository).save(captor.capture());

        boardService.play(
                PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.ONE)
                        .pitIndex(1)
                        .build()
        );

        assertThat(captor.getValue(), BoardMatcher.capturedStones(PlayerEnum.ONE));
    }

    @Test
    public void whenLastStoneGoesToEmptyPlayerTwoPit_shouldCaptureMirroredPitStones(){
        doReturn(Optional.of(BoardHelper.withEmptyPit(PlayerEnum.TWO, 2))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.ONE)).when(boardRepository).save(captor.capture());

        boardService.play(
                PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.TWO)
                        .pitIndex(1)
                        .build()
        );

        assertThat(captor.getValue(), BoardMatcher.capturedStones(PlayerEnum.TWO));
    }

    @Test
    public void whenCleanAllPlayerPits_shouldEndGame(){
        doReturn(Optional.of(BoardHelper.allCleanExceptLast(PlayerEnum.ONE))).when(boardRepository).findById(anyString());
        doReturn(BoardHelper.create(PlayerEnum.TWO)).when(boardRepository).save(captor.capture());

        boardService.play(
                PlayRequest.builder()
                        .id("1L")
                        .player(PlayerEnum.ONE)
                        .pitIndex(5)
                        .build()
        );

        assertThat(captor.getValue(), BoardMatcher.gameFinished());
    }
}
