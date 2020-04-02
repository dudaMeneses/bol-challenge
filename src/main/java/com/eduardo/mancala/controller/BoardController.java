package com.eduardo.mancala.controller;

import com.eduardo.mancala.model.request.PlayRequest;
import com.eduardo.mancala.model.response.BoardResponse;
import com.eduardo.mancala.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "boards")
@RequestMapping(path = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    @NonNull
    private BoardService boardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Create new board",
            response = BoardResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Board created successfully")
    })
    public BoardResponse create(){
        return BoardResponse.of(boardService.create());
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(
            value = "Find specific board",
            response = BoardResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Board retrieved successfully"),
            @ApiResponse(code = 404, message = "Board not found")
    })
    public BoardResponse findById(@PathVariable String id){
        return BoardResponse.of(boardService.findById(id));
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(
            value = "Play in board",
            response = BoardResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Play made successfully"),
            @ApiResponse(code = 404, message = "Board not found"),
            @ApiResponse(code = 400, message = "Validation issues")
    })
    public BoardResponse play(@PathVariable String id,
                              @Valid @RequestBody PlayRequest request){
        return BoardResponse.of(boardService.play(request.withId(id)));
    }
}
