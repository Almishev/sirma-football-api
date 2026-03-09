package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(value = """
        SELECT player1, player2, SUM(overlap) AS total_minutes
        FROM (
            SELECT
                r1.player_id AS player1,
                r2.player_id AS player2,
                GREATEST(0,
                    LEAST(COALESCE(r1.to_minutes, 90), COALESCE(r2.to_minutes, 90))
                    - GREATEST(r1.from_minutes, r2.from_minutes)
                ) AS overlap
            FROM records r1
            INNER JOIN records r2
                ON r1.match_id = r2.match_id
                AND r1.player_id < r2.player_id
            WHERE
                r1.from_minutes < COALESCE(r2.to_minutes, 90)
                AND r2.from_minutes < COALESCE(r1.to_minutes, 90)
        ) AS pair_overlaps
        GROUP BY player1, player2
        ORDER BY total_minutes DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findTopPairsWithSubquery(@Param("limit") int limit);
}
