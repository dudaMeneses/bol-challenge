package com.eduardo.mancala.model.request;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import com.eduardo.mancala.service.PitService;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Getter
@Builder
public class Distributer {
    private NextPlay next;
    private Integer stones;

    public Distributer next() {
        return this.withNext(getUpdatedNext()).withStones(getUpdatedStones());
    }

    public NextPlay getUpdatedNext() {
        if(next.getIndex().equals(PitService.KALLAH_INDEX)){
            return NextPlay.builder()
                    .player(next.getPlayer().equals(PlayerEnum.ONE) ? PlayerEnum.TWO : PlayerEnum.ONE)
                    .index(0)
                    .build();
        } else {
            return next.withIndex(next.getIndex() + 1);
        }
    }

    public Integer getUpdatedStones() {
        return this.stones - 1;
    }
}
