package com.eduardo.mancala.model.response;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("Player")
public interface SideProjection {
    List<PitProjection> getPits();
    PitProjection getKallah();
}
