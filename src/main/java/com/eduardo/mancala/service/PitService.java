package com.eduardo.mancala.service;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.entity.Pit;
import com.eduardo.mancala.model.request.DistributeParameter;
import com.eduardo.mancala.model.request.NextPlay;
import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import org.springframework.stereotype.Service;

@Service
public class PitService {

    public static final Integer KALLAH_INDEX = 6;
    public static final Integer FIRST_PIT_INDEX = 0;

    public static Integer clearPitAndGetStones(PlayRequest request, Board board) {
        Pit pit = getPit(request, board);

        Integer stones = pit.getStones();
        pit.setStones(0);
        return stones;
    }

    public static Pit getPit(NextPlay nextPlay, Board board){
        if(PlayerEnum.ONE.equals(nextPlay.getPlayer())){
            return KALLAH_INDEX.equals(nextPlay.getIndex()) ?
                    board.getPlayerOne().getKallah() :
                    board.getPlayerOne().getPits().get(nextPlay.getIndex());
        } else {
            return KALLAH_INDEX.equals(nextPlay.getIndex()) ?
                    board.getPlayerTwo().getKallah() :
                    board.getPlayerTwo().getPits().get(nextPlay.getIndex());
        }
    }

    public static Pit getPit(PlayRequest request, Board board) {
        return PlayerEnum.ONE.equals(request.getPlayer()) ?
                board.getPlayerOne().getPits().get(request.getPitIndex()) :
                board.getPlayerTwo().getPits().get(request.getPitIndex());
    }

    public static Pit getMirrored(DistributeParameter distributeParameter, Board board) {
        if(PlayerEnum.ONE.equals(distributeParameter.getNext().getPlayer())){
            return getPit(
                    NextPlay.builder()
                            .index(PitService.getMirroredIndex(distributeParameter.getNext().getIndex()))
                            .player(PlayerEnum.TWO)
                            .build(),
                    board);
        } else {
            return getPit(
                    NextPlay.builder()
                            .index(PitService.getMirroredIndex(distributeParameter.getNext().getIndex()))
                            .player(PlayerEnum.ONE)
                            .build(),
                    board);
        }
    }

    private static Integer getMirroredIndex(Integer index) {
        return 5 - index;
    }
}
