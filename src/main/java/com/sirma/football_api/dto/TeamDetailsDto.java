package com.sirma.football_api.dto;

import java.util.List;

public class TeamDetailsDto {

    private Long id;
    private String name;
    private String groupLetter;
    private String managerFullName;
    private List<PlayerShortDto> players;

    public TeamDetailsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupLetter() {
        return groupLetter;
    }

    public void setGroupLetter(String groupLetter) {
        this.groupLetter = groupLetter;
    }

    public String getManagerFullName() {
        return managerFullName;
    }

    public void setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
    }

    public List<PlayerShortDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerShortDto> players) {
        this.players = players;
    }
}

