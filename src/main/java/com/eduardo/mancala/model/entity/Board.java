package com.eduardo.mancala.model.entity;

import com.eduardo.mancala.model.request.data.PlayerEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Document(collection = "boards")
public class Board {

    @Id
    private String id;

    private List<Pit> pits;
    private PlayerEnum next;
    private boolean finished;

    public Board(){
        createInitialPits();

        this.next = PlayerEnum.ONE;
    }

    private void createInitialPits() {
        this.pits = IntStream.range(1, 15)
            .mapToObj(i -> i % 7 == 0 ? new Kallah() : new Pit())
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Integer getStones(Integer pitIndex) {
        return this.pits.get(pitIndex).getStones();
    }

    public Pit getMirrored(int pitIndex) {
        return this.pits.get(12 - pitIndex);
    }

    public boolean hasCleanLine() {
        boolean playerOneFinished = IntStream.range(0, 6)
                .mapToObj(i -> this.pits.get(i))
                .allMatch(pit -> pit.getStones().equals(0));

        boolean playerTwoFinished = IntStream.range(7, 13)
                .mapToObj(i -> this.pits.get(i))
                .allMatch(pit -> pit.getStones().equals(0));

        return playerOneFinished || playerTwoFinished;
    }

    public Pit getPlayerKallah(PlayerEnum player) {
        if(player.equals(PlayerEnum.ONE)){
            return this.pits.get(6);
        } else return this.pits.get(13);
    }

    public boolean isKallahPit(int pitIndex) {
        return this.pits.get(pitIndex).isKallah();
    }

}
