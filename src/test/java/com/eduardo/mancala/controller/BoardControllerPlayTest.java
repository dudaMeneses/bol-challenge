package com.eduardo.mancala.controller;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerPlayTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void whenPlayerOnePlayAndDoNotEndInKallah_shouldNextBePlayerTwoAndReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                            PlayRequest.builder()
                                    .player(PlayerEnum.ONE)
                                    .pitIndex(4)
                                    .build())
                        )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.next").value("TWO"))
                .andExpect(jsonPath("$.playerOne").isNotEmpty())
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(7))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(1))
                .andExpect(jsonPath("$.playerTwo").isNotEmpty())
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(7))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(7))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(7))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(7))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(0));
    }

    @Test
    public void whenPlayerTwoPlayAndDoNotEndInKallah_shouldNextBePlayerOneAndReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());
        board.setNext(PlayerEnum.TWO);
        boardRepository.save(board);

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.TWO)
                                        .pitIndex(4)
                                        .build())
                        )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.next").value("ONE"))
                .andExpect(jsonPath("$.playerOne").isNotEmpty())
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(0))
                .andExpect(jsonPath("$.playerTwo").isNotEmpty())
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(7))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(1));
    }

    @Test
    public void whenEndsInEmptyPit_shouldCaptureMirroredStonesAndReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());
        board.getPlayerOne().getPits().get(3).setStones(0);
        board.getPlayerOne().getPits().get(2).setStones(1);
        boardRepository.save(board);

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.ONE)
                                        .pitIndex(2)
                                        .build())
                        )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.next").value("TWO"))
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(0));
    }

    @Test
    public void whenCleanPlayerPits_shouldFinishGameAndReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());
        IntStream.range(0, 5).forEach(i -> board.getPlayerOne().getPits().get(i).setStones(0));
        boardRepository.save(board);

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.ONE)
                                        .pitIndex(5)
                                        .build())
                        )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.finished").value("true"))
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(0))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(1))
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(0))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(41));
    }

    @Test
    public void whenEndsInKallah_shouldSamePlayerBeNextAndReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.ONE)
                                        .pitIndex(0)
                                        .build())
                        )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.next").value("ONE"))
                .andExpect(jsonPath("$.playerOne").isNotEmpty())
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(0))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(7))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(7))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(1))
                .andExpect(jsonPath("$.playerTwo").isNotEmpty())
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(0));
    }

    @Test
    public void whenMatchNotFound_shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                put("/boards/{id}", "99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.ONE)
                                        .pitIndex(4)
                                        .build())
                        )
        ).andExpect(status().isNotFound());
    }

    @Test
    public void whenWrongPlay_shouldReturnBadRequest() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.TWO)
                                        .pitIndex(4)
                                        .build())
                        )
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenNegativeIndex_shouldReturnBadRequest() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .player(PlayerEnum.ONE)
                                        .pitIndex(-1)
                                        .build())
                        )
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenEmptyPlayer_shouldReturnBadRequest() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                put("/boards/{id}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                PlayRequest.builder()
                                        .pitIndex(0)
                                        .build())
                        )
        ).andExpect(status().isBadRequest());
    }
}
