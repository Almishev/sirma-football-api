package com.sirma.football_api.dto;

import java.util.List;

public class GroupStandingDto {

    private String groupLetter;
    private List<TeamStandingDto> teams;

    public GroupStandingDto() {
    }

    public String getGroupLetter() {
        return groupLetter;
    }

    public void setGroupLetter(String groupLetter) {
        this.groupLetter = groupLetter;
    }

    public List<TeamStandingDto> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamStandingDto> teams) {
        this.teams = teams;
    }
}

