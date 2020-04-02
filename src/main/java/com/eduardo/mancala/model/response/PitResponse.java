package com.eduardo.mancala.model.response;

import com.eduardo.mancala.model.entity.Pit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel("Pit")
public class PitResponse {

    @ApiModelProperty(example = "5")
    private Integer stones;

    public static PitResponse of(Pit pit) {
        return PitResponse.builder()
                .stones(pit.getStones())
                .build();
    }
}
