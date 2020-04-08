package com.eduardo.mancala.model.response;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Board")
public interface BoardProjection {

    @ApiModelProperty(example = "1231452312")
    String getId();

    SideProjection getPlayerOne();
    SideProjection getPlayerTwo();

    @ApiModelProperty(example = "TWO")
    PlayerEnum getNext();

    @ApiModelProperty(example = "true")
    boolean isFinished();
}
