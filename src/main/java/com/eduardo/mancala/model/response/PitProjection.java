package com.eduardo.mancala.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Pit")
public interface PitProjection {
    @ApiModelProperty(example = "5")
    Integer getStones();
}
