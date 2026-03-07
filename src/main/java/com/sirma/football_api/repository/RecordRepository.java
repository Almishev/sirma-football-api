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
    @Query("DELETE FROM Record r WHERE r.player.id = :playerId")
    void deleteByPlayerId(Long playerId);

    @Modifying
    @Query("DELETE FROM Record r WHERE r.match.id = :matchId")
    void deleteByMatchId(Long matchId);

    @Modifying
    @Query("DELETE FROM Record r WHERE r.player.id IN :playerIds")
    void deleteByPlayerIdIn(List<Long> playerIds);

    @Modifying
    @Query(value = "ALTER SEQUENCE records_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
}
