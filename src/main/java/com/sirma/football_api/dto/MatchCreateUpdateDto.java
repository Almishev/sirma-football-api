package com.sirma.football_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MatchCreateUpdateDto {

    @NotNull(message = "aTeamId must not be null")
    @JsonProperty("aTeamId")
    private Long aTeamId;

    @NotNull(message = "bTeamId must not be null")
    @JsonProperty("bTeamId")
    private Long bTeamId;

    @NotNull(message = "date must not be null")
    private LocalDate date;

    private String score;

    public MatchCreateUpdateDto() {
    }

    public Long getaTeamId() {
        return aTeamId;
    }

    public void setaTeamId(Long aTeamId) {
        this.aTeamId = aTeamId;
    }

    public Long getbTeamId() {
        return bTeamId;
    }

    public void setbTeamId(Long bTeamId) {
        this.bTeamId = bTeamId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
