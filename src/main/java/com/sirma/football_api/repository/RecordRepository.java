package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
