package com.eduardo.mancala.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GameFinishedException extends ResponseStatusException {
    public GameFinishedException(){
        super(HttpStatus.BAD_REQUEST, "Game already finished");
    }
}
