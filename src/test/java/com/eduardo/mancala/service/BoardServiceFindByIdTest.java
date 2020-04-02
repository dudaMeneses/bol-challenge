package com.eduardo.mancala.service;

import com.eduardo.mancala.helper.BoardHelper;
import com.eduardo.mancala.matcher.BoardMatcher;
import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import com.eduardo.mancala.service.exception.BoardNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BoardServiceFindByIdTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    public void whenHappyPath_shouldReturnDefaultBoard(){
        doReturn(Optional.of(BoardHelper.create(PlayerEnum.ONE))).when(boardRepository).findById(anyString());

        Board board = boardService.findById("1L");

        assertThat(board, BoardMatcher.playerOneNext());
    }

    @Test
    public void whenNotFound_shouldReturnDefaultBoard(){
        doReturn(Optional.empty()).when(boardRepository).findById(anyString());

        assertThrows(
                BoardNotFoundException.class,
                () -> boardService.findById("1L")
        );
    }

}