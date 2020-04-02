package com.eduardo.mancala.controller;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerFindByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void whenHappyPath_shouldReturnOk() throws Exception {
        Board board = boardRepository.save(new Board());

        mockMvc.perform(
                get("/boards/{id}", board.getId())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.next").value("ONE"))
                .andExpect(jsonPath("$.playerOne").isNotEmpty())
                .andExpect(jsonPath("$.playerOne.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[2].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerOne.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerOne.kallah.stones").value(0))
                .andExpect(jsonPath("$.playerTwo").isNotEmpty())
                .andExpect(jsonPath("$.playerTwo.pits[0].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[1].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[2].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[3].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[4].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.pits[5].stones").value(6))
                .andExpect(jsonPath("$.playerTwo.kallah.stones").value(0));
    }
}
