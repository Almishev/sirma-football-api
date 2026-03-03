package com.sirma.football_api.dto;

public class PlayerStatsDto {

    private Long id;
    private String fullName;
    private String position;
    private Integer teamNumber;
    private Long teamId;
    private String teamName;
    private String groupLetter;

    private int matchesPlayed;
    private int totalMinutes;
    private double averageMinutesPerMatch;

    public PlayerStatsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getGroupLetter() {
        return groupLetter;
    }

    public void setGroupLetter(String groupLetter) {
        this.groupLetter = groupLetter;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public double getAverageMinutesPerMatch() {
        return averageMinutesPerMatch;
    }

    public void setAverageMinutesPerMatch(double averageMinutesPerMatch) {
        this.averageMinutesPerMatch = averageMinutesPerMatch;
    }
}

