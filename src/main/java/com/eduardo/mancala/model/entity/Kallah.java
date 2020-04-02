package com.eduardo.mancala.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Kallah extends Pit {

    public Kallah(){
        super();
        this.stones = 0;
    }

    @Override
    public boolean isKallah() {
        return true;
    }
}
