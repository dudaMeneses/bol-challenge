package com.eduardo.mancala.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongPlayException extends ResponseStatusException {
    public WrongPlayException(){
        super(HttpStatus.BAD_REQUEST, "The player provided is not the next one to play");
    }
}
