package com.eduardo.mancala.helper;

import com.eduardo.mancala.model.entity.Pit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PitHelper {
    public static Pit empty() {
        return new Pit().withStones(0);
    }
}
