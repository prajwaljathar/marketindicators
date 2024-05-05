package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.HistoricalDataInRangeEntity;

@Repository
public interface HistoricalDataInRangeRepository extends JpaRepository<HistoricalDataInRangeEntity, Long>{

	List<InstrumentRepository> findByInstrumentKeyAndDateBetween(String instrumentKey, LocalDate startDate,
			LocalDate endDate);

	List<HistoricalDataInRangeEntity> findByInstrumentKeyOrderByDateDesc(String instrumentKey);

}
