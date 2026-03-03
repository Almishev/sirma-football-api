package com.sirma.football_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class MatchDetailsDto {

    private Long id;
    private LocalDate date;
    private String score;

    @JsonProperty("aTeam")
    private TeamSideDto aTeam;

    @JsonProperty("bTeam")
    private TeamSideDto bTeam;

    public MatchDetailsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TeamSideDto getaTeam() {
        return aTeam;
    }

    public void setaTeam(TeamSideDto aTeam) {
        this.aTeam = aTeam;
    }

    public TeamSideDto getbTeam() {
        return bTeam;
    }

    public void setbTeam(TeamSideDto bTeam) {
        this.bTeam = bTeam;
    }
}

