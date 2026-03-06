package com.sirma.football_api.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImportService {

    int importTeams(MultipartFile file) throws IOException;

    int importPlayers(MultipartFile file) throws IOException;

    int importMatches(MultipartFile file) throws IOException;

    int importRecords(MultipartFile file) throws IOException;

    int deleteRecords();

    int deleteMatches();

    int deletePlayers();

    int deleteTeams();

    Map<String, Integer> deleteAll();
}

