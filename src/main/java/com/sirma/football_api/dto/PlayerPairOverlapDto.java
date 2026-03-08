package com.sirma.football_api.dto;

public class PlayerPairOverlapDto {

    private Long player1Id;
    private Long player2Id;
    private Long totalMinutes;
    private String player1Name;
    private String player2Name;

    public PlayerPairOverlapDto() {
    }

    public PlayerPairOverlapDto(Long player1Id, Long player2Id, Long totalMinutes) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.totalMinutes = totalMinutes;
    }

    public PlayerPairOverlapDto(Long player1Id, Long player2Id, Long totalMinutes, String player1Name, String player2Name) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.totalMinutes = totalMinutes;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }
}
