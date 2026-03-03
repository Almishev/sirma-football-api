package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByMatchId(Long matchId);
}
