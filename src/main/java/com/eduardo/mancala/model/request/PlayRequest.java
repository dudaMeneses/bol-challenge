package com.eduardo.mancala.model.request;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Builder
@ApiModel("Play")
public class PlayRequest {

    @With
    @ApiModelProperty(hidden = true)
    private String id;

    @NotNull
    @ApiModelProperty(example = "ONE")
    private PlayerEnum player;

    @Max(5)
    @NotNull
    @PositiveOrZero
    @ApiModelProperty(example = "5")
    private Integer pitIndex;

}
