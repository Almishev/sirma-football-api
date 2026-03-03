package com.sirma.football_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class MatchSummaryDto {

    private Long id;
    private LocalDate date;
    private String groupLetter;

    @JsonProperty("aTeamName")
    private String aTeamName;

    @JsonProperty("bTeamName")
    private String bTeamName;

    private String score;

    public MatchSummaryDto() {
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

    public String getGroupLetter() {
        return groupLetter;
    }

    public void setGroupLetter(String groupLetter) {
        this.groupLetter = groupLetter;
    }

    public String getaTeamName() {
        return aTeamName;
    }

    public void setaTeamName(String aTeamName) {
        this.aTeamName = aTeamName;
    }

    public String getbTeamName() {
        return bTeamName;
    }

    public void setbTeamName(String bTeamName) {
        this.bTeamName = bTeamName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

