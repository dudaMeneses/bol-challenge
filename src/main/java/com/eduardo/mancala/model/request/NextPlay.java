package com.eduardo.mancala.model.request;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Getter
@Builder
public class NextPlay {
    private Integer index;
    private PlayerEnum player;
}
