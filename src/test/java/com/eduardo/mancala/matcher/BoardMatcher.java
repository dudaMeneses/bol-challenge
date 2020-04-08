package com.eduardo.mancala.matcher;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public class BoardMatcher {
    public static Matcher<Board> playerTwoNext() {
        return hasProperty("next", equalTo(PlayerEnum.TWO));

    }

    public static Matcher<Board> playerOneNext() {
        return hasProperty("next", equalTo(PlayerEnum.ONE));
    }

    public static Matcher<Board> capturedStones(PlayerEnum player) {
        return hasProperty(PlayerEnum.ONE.equals(player) ? "playerOne" : "playerTwo",
                    hasProperty("pits", hasItem(
                            hasProperty("stones", equalTo(7))
                    )
                )
        );
    }

    public static Matcher<Board> gameFinished() {
        return hasProperty("finished", equalTo(true));
    }
}
