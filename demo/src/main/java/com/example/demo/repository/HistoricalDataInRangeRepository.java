package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.HistoricalDataInRangeEntity;

@Repository
public interface HistoricalDataInRangeRepository extends JpaRepository<HistoricalDataInRangeEntity, Long>{

}
