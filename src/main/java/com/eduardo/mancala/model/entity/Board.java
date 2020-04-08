package com.eduardo.mancala.model.entity;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "boards")
public class Board {

    @Id
    private String id;

    private Line playerOne;
    private Line playerTwo;

    private PlayerEnum next;
    private boolean finished;

    public Board(){
        this.playerOne = new Line();
        this.playerTwo = new Line();
        this.next = PlayerEnum.ONE;
    }

    public boolean hasCleanLine() {
        return playerOne.hasCleanLine() || playerTwo.hasCleanLine();
    }

    public void changeNext() {
        if(this.next.equals(PlayerEnum.ONE)){
            this.next = PlayerEnum.TWO;
        } else {
            this.next = PlayerEnum.ONE;
        }
    }

    public void finishGame() {
        this.playerOne.finish();
        this.playerTwo.finish();
        this.finished = true;
    }
}
