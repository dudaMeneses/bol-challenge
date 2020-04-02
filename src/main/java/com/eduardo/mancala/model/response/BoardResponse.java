package com.eduardo.mancala.model.response;

import com.eduardo.mancala.model.entity.Board;
import com.eduardo.mancala.model.entity.Pit;
import com.eduardo.mancala.model.request.data.PlayerEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ApiModel("Board")
public class BoardResponse {

    @ApiModelProperty(example = "1231452312")
    private String id;

    private SideResponse playerOne;
    private SideResponse playerTwo;

    @ApiModelProperty(example = "TWO")
    private PlayerEnum next;

    @ApiModelProperty(example = "true")
    private boolean finished;

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .playerOne(getSide(board.getPits().stream()
                        .limit(7)
                        .collect(Collectors.toList())))
                .playerTwo(getSide(board.getPits().stream()
                        .skip(7)
                        .limit(7)
                        .collect(Collectors.toList())))
                .next(board.getNext())
                .finished(board.isFinished())
                .build();
    }

    private static SideResponse getSide(List<Pit> pits) {
        return SideResponse.builder()
                .kallah(pits.stream()
                        .filter(Pit::isKallah)
                        .findFirst()
                        .map(PitResponse::of)
                        .orElse(null))
                .pits(pits.stream()
                        .filter(pit -> !pit.isKallah())
                        .map(PitResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
