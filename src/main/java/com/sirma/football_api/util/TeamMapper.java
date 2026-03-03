package com.sirma.football_api.util;

import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.dto.TeamDetailsDto;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Team;

import java.util.ArrayList;
import java.util.List;

public final class TeamMapper {

    private TeamMapper() {
    }

    public static TeamDetailsDto toTeamDetailsDto(Team team, List<Player> players) {
        List<PlayerShortDto> playerDtos = new ArrayList<>(players.size());
        for (Player player : players) {
            PlayerShortDto dto = new PlayerShortDto();
            dto.setId(player.getId());
            dto.setTeamNumber(player.getTeamNumber());
            dto.setFullName(player.getFullName());
            dto.setPosition(player.getPosition());
            playerDtos.add(dto);
        }

        TeamDetailsDto dto = new TeamDetailsDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setGroupLetter(team.getGroupLetter());
        dto.setManagerFullName(team.getManagerFullName());
        dto.setPlayers(playerDtos);

        return dto;
    }
}

