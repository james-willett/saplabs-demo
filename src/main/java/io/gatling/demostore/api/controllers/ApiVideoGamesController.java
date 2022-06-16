package io.gatling.demostore.api.controllers;

import io.gatling.demostore.api.payloads.ProductRequest;
import io.gatling.demostore.api.payloads.VideoGameRequest;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.VideoGameRepository;
import io.gatling.demostore.models.data.Product;
import io.gatling.demostore.models.data.VideoGame;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/videogame")
public class ApiVideoGamesController {

    public ApiVideoGamesController(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    private final VideoGameRepository videoGameRepository;

    @Operation(summary = "List all video games")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<VideoGame> listVideoGames() {
        return videoGameRepository.findAll();
    }

    @Operation(summary = "Get a video game")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "Video game not found")
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public VideoGame getVideoGame(
            @PathVariable @Parameter(description = "VideoGame ID") Integer id
    ) {
        return videoGameRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a video game")
    @ApiResponses(
            @ApiResponse(responseCode = "400", description = "Invalid request content or duplicate of an existing video game")
    )
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public VideoGame create(
            @Valid @RequestBody VideoGameRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // TODO - add handling for if a game exists
//        boolean videoGameExists = videoGameRepository.findById(request.getCategoryId()).isPresent();
//        if (productExists || !categoryExists) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }

        VideoGame videoGame = new VideoGame();
        videoGame.setName(request.getName());
        videoGame.setReleaseDate(request.getReleaseDate());
        videoGame.setReviewScore(request.getReviewScore());
        videoGame.setCategory(request.getCategory());
        videoGame.setRating(request.getRating());

        // DO NOT SAVE (readonly)
        return videoGame;
    }

    @Operation(summary = "Update a video game")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid request content"),
            @ApiResponse(responseCode = "404", description = "Video game not found")
    })
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public VideoGame update(
            @PathVariable @Parameter(description = "VideoGame ID") Integer id,
            @Valid @RequestBody VideoGameRequest request,
            BindingResult bindingResult
    ) {
        VideoGame videoGame = videoGameRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        videoGame.setName(request.getName());
        videoGame.setReleaseDate(request.getReleaseDate());
        videoGame.setReviewScore(request.getReviewScore());
        videoGame.setCategory(request.getCategory());
        videoGame.setRating(request.getRating());

        // DO NO SAVE (readonly)
        return videoGame;
    }

    @Operation(summary = "Delete a video game")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "Video game not found")
    )
    @DeleteMapping(path = "/{id}")
    public String delete(
            @PathVariable @Parameter(description = "VideoGame ID") Integer id
    ) {
        VideoGame videoGame = videoGameRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Do not actually delete the game (readonly)
        return "Video game deleted";
    }
}
