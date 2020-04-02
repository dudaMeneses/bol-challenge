package com.eduardo.mancala.model.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@ApiModel("Player")
public class SideResponse {
    private List<PitResponse> pits;
    private PitResponse kallah;

}
