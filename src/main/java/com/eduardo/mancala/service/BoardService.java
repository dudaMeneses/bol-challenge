package com.eduardo.mancala.service;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.entity.Pit;
import com.eduardo.mancala.model.request.DistributeParameter;
import com.eduardo.mancala.model.request.NextPlay;
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
        validatePitStones(board, request);

        DistributeParameter parameter = DistributeParameter.builder()
                .next(NextPlay.builder()
                        .index(request.getPitIndex() + 1)
                        .player(request.getPlayer())
                        .build())
                .stones(PitService.clearPitAndGetStones(request, board))
                .build();

        return boardRepository.save(distributeStones(board, parameter));
    }

    private Board distributeStones(Board board, DistributeParameter distributeParameter) {
        if(distributeParameter.getStones() > 0) {
            updatePitStones(board, distributeParameter);
            return distributeStones(
                    board,
                    distributeParameter
                            .withNext(getNext(distributeParameter.getNext()))
                            .withStones(distributeParameter.getStones() - 1)
            );
        }

        if(endedInKallah(distributeParameter)){
            board.changeNext();
        }

        if(board.hasCleanLine()){
            board.finishGame();
        }

        return board;
    }

    private boolean endedInKallah(DistributeParameter distributeParameter) {
        return !PitService.FIRST_PIT_INDEX.equals(distributeParameter.getNext().getIndex());
    }

    private NextPlay getNext(NextPlay next) {
        if(next.getIndex().equals(PitService.KALLAH_INDEX)){
            return NextPlay.builder()
                    .player(next.getPlayer().equals(PlayerEnum.ONE) ? PlayerEnum.TWO : PlayerEnum.ONE)
                    .index(0)
                    .build();
        } else {
            return next.withIndex(next.getIndex() + 1);
        }
    }

    private void updatePitStones(Board board, DistributeParameter distributeParameter) {
        Pit pit = PitService.getPit(distributeParameter.getNext(), board);

        if(distributeParameter.getStones() == 1 && pit.getStones().equals(0) && !pit.isKallah()){
            pit.setStones(captureStones(board, distributeParameter));
        } else {
            pit.setStones(pit.getStones() + 1);
        }
    }

    private Integer captureStones(Board board, DistributeParameter distributeParameter) {
        Pit mirrored = PitService.getMirrored(distributeParameter, board);
        Integer stones = mirrored.getStones() + 1;
        mirrored.setStones(0);

        return stones;
    }

    private Board load(final String id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
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
