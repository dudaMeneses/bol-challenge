package com.eduardo.mancala.service;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.entity.Pit;
import com.eduardo.mancala.model.request.Distributer;
import com.eduardo.mancala.model.request.NextPlay;
import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.repository.BoardRepository;
import com.eduardo.mancala.service.exception.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        validatePlay(request, board);

        Distributer parameter = Distributer.builder()
                .next(NextPlay.builder()
                        .index(request.getPitIndex() + 1)
                        .player(request.getPlayer())
                        .build())
                .stones(PitService.clearPitAndGetStones(request, board))
                .build();

        return boardRepository.save(distributeStones(board, parameter));
    }

    private Board distributeStones(Board board, Distributer distributer) {
        if(distributer.getStones() > 0) {
            updatePitStones(board, distributer);
            return distributeStones(board, distributer.next());
        }

        if(endedInKallah(distributer)){
            board.changeNext();
        }

        if(board.hasCleanLine()){
            board.finishGame();
        }

        return board;
    }

    private boolean endedInKallah(Distributer distributer) {
        return !PitService.FIRST_PIT_INDEX.equals(distributer.getNext().getIndex());
    }

    private void updatePitStones(Board board, Distributer distributer) {
        Pit pit = PitService.getPit(distributer.getNext(), board);

        if(distributer.getStones() == 1 && pit.getStones().equals(0) && !pit.isKallah()){
            pit.setStones(PitService.captureStones(board, distributer));
        } else {
            pit.setStones(pit.getStones() + 1);
        }
    }

    private Board load(final String id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
    }

    private void validatePlay(PlayRequest request, Board board) {
        validateGameFinished(board);
        validatePlayer(board, request.getPlayer());
        validateIsKallah(request);
        validatePitStones(board, request);
    }

    private void validateIsKallah(PlayRequest request) {
        if(request.getPitIndex().equals(PitService.KALLAH_INDEX)){
            throw new IllegalPlayException();
        }
    }

    private void validatePitStones(Board board, PlayRequest request) {
        if(PitService.getPit(request, board).getStones() < 1){
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
