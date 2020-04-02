package com.eduardo.mancala.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@With
@Data
@AllArgsConstructor
public class Pit {
    protected Integer stones;

    public Pit(){
        this.stones = 6;
    }

    public boolean isKallah(){
        return false;
    }
}
