package com.eduardo.mancala.helper;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import lombok.experimental.UtilityClass;

import java.util.stream.IntStream;

@UtilityClass
public class BoardHelper {
    public static Board create(PlayerEnum player) {
        if(player.equals(PlayerEnum.ONE))
            return new Board();
        else {
            Board board = new Board();
            board.setNext(PlayerEnum.TWO);
            return board;
        }
    }

    public static Board withEmptyPit(PlayerEnum player, int emptyPit) {
        Board board = create(player);
        board.getPits().get(emptyPit - 1).setStones(1);
        board.getPits().get(emptyPit).setStones(0);

        return board;
    }

    public static Board finished() {
        Board board = create(PlayerEnum.ONE);
        board.setFinished(true);

        return board;
    }

    public static Board allCleanExceptLast(PlayerEnum player) {
        Board board = create(player);
        IntStream.range(0, 5).forEach(i -> board.getPits().get(i).setStones(0));
        return board;
    }
}
