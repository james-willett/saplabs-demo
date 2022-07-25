package io.videogamedb.api.payloads;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class VideoGameRequest {

    @Schema(example = "Mario", required = true)
    @NotNull(message = "Please set a name")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @Schema(example = "2012-05-04", required = true)
    @NotNull(message = "1990-01-01")
    private String releaseDate;

    @Schema(example = "85", required = true)
    @NotNull(message = "Please set a review score")
    private Integer reviewScore;

    @Schema(example = "Platform", required = true)
    @NotNull(message = "Please set a category")
    private String category;

    @Schema(example = "Mature", required = true)
    @NotNull(message = "Please set a rating")
    private String rating;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getReviewScore() {
        return this.reviewScore;
    }

    public void setReviewScore(Integer reviewScore) {
        this.reviewScore = reviewScore;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
