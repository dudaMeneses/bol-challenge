package com.eduardo.mancala.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyPitException extends ResponseStatusException {
    public EmptyPitException(){
        super(HttpStatus.BAD_REQUEST, "Selected pit has no stones");
    }
}
