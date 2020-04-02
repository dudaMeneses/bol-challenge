package com.eduardo.mancala.service;

import com.eduardo.mancala.helper.BoardHelper;
import com.eduardo.mancala.matcher.BoardMatcher;
import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BoardServiceCreateTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    public void whenHappyPath_shouldReturnDefaultBoard(){
        doReturn(BoardHelper.create(PlayerEnum.ONE)).when(boardRepository).save(any(Board.class));

        Board board = boardService.create();

        assertThat(board, BoardMatcher.playerOneNext());
    }

}