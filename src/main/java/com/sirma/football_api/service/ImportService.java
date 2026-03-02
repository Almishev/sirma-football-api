package com.sirma.football_api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {

    int importTeams(MultipartFile file) throws IOException;

    int importPlayers(MultipartFile file) throws IOException;

    int importMatches(MultipartFile file) throws IOException;

    int importRecords(MultipartFile file) throws IOException;
}
