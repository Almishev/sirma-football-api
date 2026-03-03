package com.sirma.football_api.util;

import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;
import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.dto.TeamSideDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Record;
import com.sirma.football_api.entity.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MatchMapper {

    private MatchMapper() {
    }

    public static MatchSummaryDto toMatchSummaryDto(Match match) {
        MatchSummaryDto dto = new MatchSummaryDto();
        dto.setId(match.getId());
        dto.setDate(match.getDate());
        Team aTeam = match.getaTeam();
        Team bTeam = match.getbTeam();
        dto.setGroupLetter(aTeam != null ? aTeam.getGroupLetter() : null);
        dto.setaTeamName(aTeam != null ? aTeam.getName() : null);
        dto.setbTeamName(bTeam != null ? bTeam.getName() : null);
        dto.setScore(match.getScore());
        return dto;
    }

    public static MatchDetailsDto toMatchDetailsDto(Match match, List<Record> records) {
        Map<Long, List<Player>> playersByTeamId = new HashMap<>();
        for (Record record : records) {
            Player player = record.getPlayer();
            if (player == null || player.getTeam() == null) {
                continue;
            }
            Long teamId = player.getTeam().getId();
            playersByTeamId
                    .computeIfAbsent(teamId, key -> new ArrayList<>())
                    .add(player);
        }

        MatchDetailsDto dto = new MatchDetailsDto();
        dto.setId(match.getId());
        dto.setDate(match.getDate());
        dto.setScore(match.getScore());

        Team aTeam = match.getaTeam();
        Team bTeam = match.getbTeam();

        dto.setaTeam(buildTeamSide(aTeam, playersByTeamId.get(aTeam != null ? aTeam.getId() : null)));
        dto.setbTeam(buildTeamSide(bTeam, playersByTeamId.get(bTeam != null ? bTeam.getId() : null)));

        return dto;
    }

    private static TeamSideDto buildTeamSide(Team team, List<Player> players) {
        TeamSideDto dto = new TeamSideDto();
        if (team != null) {
            dto.setId(team.getId());
            dto.setName(team.getName());
            dto.setGroupLetter(team.getGroupLetter());
        }
        List<PlayerShortDto> playerDtos = new ArrayList<>();
        if (players != null) {
            Map<Long, PlayerShortDto> uniqueById = new HashMap<>();
            players.sort(Comparator.comparing(
                    Player::getTeamNumber,
                    Comparator.nullsLast(Comparator.naturalOrder())));
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                Long playerId = player.getId();
                if (!uniqueById.containsKey(playerId)) {
                    PlayerShortDto p = new PlayerShortDto();
                    p.setId(playerId);
                    p.setTeamNumber(player.getTeamNumber());
                    p.setFullName(player.getFullName());
                    p.setPosition(player.getPosition());
                    uniqueById.put(playerId, p);
                }
            }
            playerDtos.addAll(uniqueById.values());
        }
        dto.setPlayers(playerDtos);
        return dto;
    }
}

