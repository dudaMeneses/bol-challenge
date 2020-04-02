package com.eduardo.mancala.service;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.entity.Pit;
import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import com.eduardo.mancala.service.exception.BoardNotFoundException;
import com.eduardo.mancala.service.exception.EmptyPitException;
import com.eduardo.mancala.service.exception.GameFinishedException;
import com.eduardo.mancala.service.exception.WrongPlayException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoardService {

    @NonNull
    private BoardRepository boardRepository;

    public Board create() {
        return boardRepository.save(new Board());
    }

    public Board findById(final String id) {
        return load(id);
    }

    public Board play(final PlayRequest request) {
        Board board = load(request.getId());

        validateGameFinished(board);
        validatePlayer(board, request.getPlayer());
        validatePitStones(board, getPitIndex(request));

        return boardRepository.save(distributeStones(board, getPitIndex(request), clearPitAndGetStones(getPitIndex(request), board)));
    }

    private Board distributeStones(Board board, int pitIndex, Integer stones) {
        if(stones > 0) {
            pitIndex = getNextPit(pitIndex);
            updatePitStones(board, pitIndex, stones);
            return distributeStones(board, pitIndex, --stones);
        }

        if(!board.isKallahPit(pitIndex)){
            changeNext(board);
        }

        if(board.hasCleanLine()){
            finishGame(board);
        }

        return board;
    }

    private void finishGame(Board board) {
        board.getPlayerKallah(PlayerEnum.ONE).setStones(getPlayerSum(board, 0, 7));
        board.getPlayerKallah(PlayerEnum.TWO).setStones(getPlayerSum(board, 7, 14));
        board.setFinished(true);
    }

    private int getPlayerSum(Board board, int start, int end) {
        return IntStream.range(start, end)
                .mapToObj(i -> board.getPits().get(i))
                .mapToInt(pit -> {
                    Integer stones = pit.getStones();
                    pit.setStones(0);
                    return stones;
                })
                .sum();
    }

    private void updatePitStones(Board board, int pitIndex, int stones) {
        Pit pit = board.getPits().get(pitIndex);
        if(stones == 1 && pit.getStones().equals(0) && !pit.isKallah()){
            pit.setStones(captureStones(board, pitIndex));
        } else {
            pit.setStones(pit.getStones() + 1);
        }
    }

    private Integer captureStones(Board board, int pitIndex) {
        Pit mirrored = board.getMirrored(pitIndex);
        Integer stones = mirrored.getStones() + 1;
        mirrored.setStones(0);

        return stones;
    }

    private int getNextPit(int pitIndex) {
        if(pitIndex != 13)
            ++pitIndex;
        else
            pitIndex = 0;
        return pitIndex;
    }

    private void changeNext(Board board) {
        if(board.getNext().equals(PlayerEnum.ONE)){
            board.setNext(PlayerEnum.TWO);
        } else {
            board.setNext(PlayerEnum.ONE);
        }
    }

    private Integer clearPitAndGetStones(int pitIndex, Board board) {
        Integer stones = board.getStones(pitIndex);
        board.getPits().get(pitIndex).setStones(0);
        return stones;
    }

    private Board load(final String id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
    }

    private int getPitIndex(PlayRequest request) {
        if(request.getPlayer().equals(PlayerEnum.ONE))
            return request.getPitIndex();
        else return 7 + request.getPitIndex();
    }

    private void validatePitStones(Board board, int pitIndex) {
        if(board.getPits().get(pitIndex).getStones() < 1){
            throw new EmptyPitException();
        }
    }

    private void validatePlayer(Board board, PlayerEnum player) {
        if(!board.getNext().equals(player)){
            throw new WrongPlayException();
        }
    }

    private void validateGameFinished(Board board) {
        if(board.isFinished()){
            throw new GameFinishedException();
        }
    }
}
