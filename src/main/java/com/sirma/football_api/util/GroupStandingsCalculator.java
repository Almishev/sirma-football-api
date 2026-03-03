package com.sirma.football_api.util;

import com.sirma.football_api.dto.GroupStandingDto;
import com.sirma.football_api.dto.TeamStandingDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class GroupStandingsCalculator {

    private GroupStandingsCalculator() {
    }

    public static List<GroupStandingDto> calculateStandings(List<Team> teams, List<Match> matches) {
        Map<Long, TeamStandingDto> standingsByTeamId = new HashMap<>();

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            TeamStandingDto standing = new TeamStandingDto();
            standing.setTeamId(team.getId());
            standing.setTeamName(team.getName());
            standing.setGroupLetter(team.getGroupLetter());
            standingsByTeamId.put(team.getId(), standing);
        }

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            Team aTeam = match.getaTeam();
            Team bTeam = match.getbTeam();
            if (aTeam == null || bTeam == null || match.getScore() == null) {
                continue;
            }

            String score = match.getScore().trim();
            String[] parts = score.split("-");
            if (parts.length != 2) {
                continue;
            }

            Integer aGoals = parseGoals(parts[0]);
            Integer bGoals = parseGoals(parts[1]);
            if (aGoals == null || bGoals == null) {
                continue;
            }

            TeamStandingDto aStanding = standingsByTeamId.get(aTeam.getId());
            TeamStandingDto bStanding = standingsByTeamId.get(bTeam.getId());
            if (aStanding == null || bStanding == null) {
                continue;
            }

            aStanding.setPlayed(aStanding.getPlayed() + 1);
            bStanding.setPlayed(bStanding.getPlayed() + 1);

            aStanding.setGoalsFor(aStanding.getGoalsFor() + aGoals);
            aStanding.setGoalsAgainst(aStanding.getGoalsAgainst() + bGoals);
            bStanding.setGoalsFor(bStanding.getGoalsFor() + bGoals);
            bStanding.setGoalsAgainst(bStanding.getGoalsAgainst() + aGoals);

            if (aGoals > bGoals) {
                aStanding.setWon(aStanding.getWon() + 1);
                aStanding.setPoints(aStanding.getPoints() + 3);
                bStanding.setLost(bStanding.getLost() + 1);
            } else if (aGoals < bGoals) {
                bStanding.setWon(bStanding.getWon() + 1);
                bStanding.setPoints(bStanding.getPoints() + 3);
                aStanding.setLost(aStanding.getLost() + 1);
            } else {
                aStanding.setDrawn(aStanding.getDrawn() + 1);
                bStanding.setDrawn(bStanding.getDrawn() + 1);
                aStanding.setPoints(aStanding.getPoints() + 1);
                bStanding.setPoints(bStanding.getPoints() + 1);
            }
        }

        List<TeamStandingDto> standingsList = new ArrayList<>(standingsByTeamId.values());
        for (int i = 0; i < standingsList.size(); i++) {
            TeamStandingDto standing = standingsList.get(i);
            int goalDiff = standing.getGoalsFor() - standing.getGoalsAgainst();
            standing.setGoalDifference(goalDiff);
        }

        Map<String, List<TeamStandingDto>> byGroup = standingsList.stream()
                .collect(Collectors.groupingBy(TeamStandingDto::getGroupLetter));

        List<GroupStandingDto> result = new ArrayList<>();
        List<Map.Entry<String, List<TeamStandingDto>>> groupEntries = new ArrayList<>(byGroup.entrySet());
        for (int i = 0; i < groupEntries.size(); i++) {
            Map.Entry<String, List<TeamStandingDto>> entry = groupEntries.get(i);
            List<TeamStandingDto> sortedTeams = new ArrayList<>(entry.getValue());
            sortedTeams.sort(Comparator
                    .comparingInt(TeamStandingDto::getPoints).reversed()
                    .thenComparingInt(TeamStandingDto::getGoalDifference).reversed()
                    .thenComparingInt(TeamStandingDto::getGoalsFor).reversed()
                    .thenComparing(TeamStandingDto::getTeamName));

            GroupStandingDto groupStanding = new GroupStandingDto();
            groupStanding.setGroupLetter(entry.getKey());
            groupStanding.setTeams(sortedTeams);
            result.add(groupStanding);
        }

        result.sort(Comparator.comparing(GroupStandingDto::getGroupLetter));
        return result;
    }

    private static Integer parseGoals(String part) {
        String trimmed = part.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        int parenIndex = trimmed.indexOf('(');
        if (parenIndex > 0) {
            trimmed = trimmed.substring(0, parenIndex);
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

