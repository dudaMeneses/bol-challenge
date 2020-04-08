package com.eduardo.mancala.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Getter
@Builder
public class DistributeParameter {
    private NextPlay next;
    private Integer stones;
}
