package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByMatchId(Long matchId);

    List<Record> findByPlayerId(Long playerId);

    @Modifying
    @Query(value = "ALTER SEQUENCE records_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
}
