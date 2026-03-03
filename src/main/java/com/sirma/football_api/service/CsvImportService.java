package com.sirma.football_api.service;

import com.sirma.football_api.dto.MatchCsvDto;
import com.sirma.football_api.dto.PlayerCsvDto;
import com.sirma.football_api.dto.RecordCsvDto;
import com.sirma.football_api.dto.TeamCsvDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Record;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.repository.MatchRepository;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.ImportService;
import com.sirma.football_api.util.DateParseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService implements ImportService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final RecordRepository recordRepository;

    public CsvImportService(
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            MatchRepository matchRepository,
            RecordRepository recordRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    @Transactional
    public int importTeams(MultipartFile file) throws IOException {
        List<TeamCsvDto> rows = parseTeams(file.getInputStream());
        for (TeamCsvDto dto : rows) {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("teams.csv row missing ID");
            }
            Team team = new Team();
            team.setName(dto.getName());
            team.setManagerFullName(dto.getManagerFullName());
            team.setGroupLetter(dto.getGroupLetter());
            teamRepository.save(team);
        }
        return rows.size();
    }

    @Override
    @Transactional
    public int importPlayers(MultipartFile file) throws IOException {
        List<PlayerCsvDto> rows = parsePlayers(file.getInputStream());
        for (PlayerCsvDto dto : rows) {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("players.csv row missing ID");
            }
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalStateException("Team not found for id: " + dto.getTeamId() + ". Import teams first."));
            Player player = new Player();
            player.setTeamNumber(dto.getTeamNumber());
            player.setPosition(dto.getPosition());
            player.setFullName(dto.getFullName());
            player.setTeam(team);
            playerRepository.save(player);
        }
        return rows.size();
    }

    @Override
    @Transactional
    public int importMatches(MultipartFile file) throws IOException {
        List<MatchCsvDto> rows = parseMatches(file.getInputStream());
        for (MatchCsvDto dto : rows) {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("matches.csv row missing ID");
            }
            Team aTeam = teamRepository.findById(dto.getaTeamId())
                    .orElseThrow(() -> new IllegalStateException("Team not found for ATeamID: " + dto.getaTeamId() + ". Import teams first."));
            Team bTeam = teamRepository.findById(dto.getbTeamId())
                    .orElseThrow(() -> new IllegalStateException("Team not found for BTeamID: " + dto.getbTeamId() + ". Import teams first."));
            LocalDate date = DateParseUtil.parse(dto.getDateString());
            Match match = new Match();
            match.setaTeam(aTeam);
            match.setbTeam(bTeam);
            match.setDate(date);
            match.setScore(dto.getScore());
            matchRepository.save(match);
        }
        return rows.size();
    }

    @Override
    @Transactional
    public int importRecords(MultipartFile file) throws IOException {
        List<RecordCsvDto> rows = parseRecords(file.getInputStream());
        List<Record> records = new ArrayList<>();
        for (RecordCsvDto dto : rows) {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("records.csv row missing ID");
            }
            Player player = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new IllegalStateException("Player not found for id: " + dto.getPlayerId() + ". Import players first."));
            Match match = matchRepository.findById(dto.getMatchId())
                    .orElseThrow(() -> new IllegalStateException("Match not found for id: " + dto.getMatchId() + ". Import matches first."));
            Record record = new Record();
            record.setPlayer(player);
            record.setMatch(match);
            record.setFromMinutes(dto.getFromMinutes());
            record.setToMinutes(dto.getToMinutes());
            records.add(record);
        }
        recordRepository.saveAll(records);
        return rows.size();
    }

    private List<String[]> readCsvLines(InputStream inputStream) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(",");
                for (int i = 0; i < columns.length; i++) {
                    String value = columns[i].trim();
                    if ("NULL".equalsIgnoreCase(value)) {
                        columns[i] = null;
                    } else {
                        columns[i] = value;
                    }
                }
                rows.add(columns);
            }
        }
        return rows;
    }

    private List<TeamCsvDto> parseTeams(InputStream inputStream) throws IOException {
        List<String[]> rows = readCsvLines(inputStream);
        List<TeamCsvDto> result = new ArrayList<>();
        for (String[] columns : rows) {
            if (columns.length < 4) {
                throw new IllegalArgumentException("Invalid teams.csv row: expected 4 columns");
            }
            TeamCsvDto dto = new TeamCsvDto();
            dto.setId(parseLong(columns[0]));
            dto.setName(columns[1]);
            dto.setManagerFullName(columns[2]);
            dto.setGroupLetter(columns[3]);
            result.add(dto);
        }
        return result;
    }

    private List<PlayerCsvDto> parsePlayers(InputStream inputStream) throws IOException {
        List<String[]> rows = readCsvLines(inputStream);
        List<PlayerCsvDto> result = new ArrayList<>();
        for (String[] columns : rows) {
            if (columns.length < 5) {
                throw new IllegalArgumentException("Invalid players.csv row: expected 5 columns");
            }
            PlayerCsvDto dto = new PlayerCsvDto();
            dto.setId(parseLong(columns[0]));
            dto.setTeamNumber(parseInteger(columns[1]));
            dto.setPosition(columns[2]);
            dto.setFullName(columns[3]);
            dto.setTeamId(parseLong(columns[4]));
            result.add(dto);
        }
        return result;
    }

    private List<MatchCsvDto> parseMatches(InputStream inputStream) throws IOException {
        List<String[]> rows = readCsvLines(inputStream);
        List<MatchCsvDto> result = new ArrayList<>();
        for (String[] columns : rows) {
            if (columns.length < 5) {
                throw new IllegalArgumentException("Invalid matches.csv row: expected 5 columns");
            }
            MatchCsvDto dto = new MatchCsvDto();
            dto.setId(parseLong(columns[0]));
            dto.setaTeamId(parseLong(columns[1]));
            dto.setbTeamId(parseLong(columns[2]));
            dto.setDateString(columns[3]);
            dto.setScore(columns[4]);
            result.add(dto);
        }
        return result;
    }

    private List<RecordCsvDto> parseRecords(InputStream inputStream) throws IOException {
        List<String[]> rows = readCsvLines(inputStream);
        List<RecordCsvDto> result = new ArrayList<>();
        for (String[] columns : rows) {
            if (columns.length < 5) {
                throw new IllegalArgumentException("Invalid records.csv row: expected 5 columns");
            }
            RecordCsvDto dto = new RecordCsvDto();
            dto.setId(parseLong(columns[0]));
            dto.setPlayerId(parseLong(columns[1]));
            dto.setMatchId(parseLong(columns[2]));
            dto.setFromMinutes(parseInteger(columns[3]));
            dto.setToMinutes(parseInteger(columns[4]));
            result.add(dto);
        }
        return result;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.valueOf(value);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.valueOf(value);
    }
}
