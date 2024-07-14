package com.example.demo.repository;

import com.example.demo.model.HistoricalCandles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoricalCandlesRepository extends JpaRepository<HistoricalCandles, Long> {

//Changed temporarily
//    @Query("SELECT MAX(c.candleTime) FROM HistoricalCandles c")
//    LocalDateTime findHighestDate();
    
    

	/*
	 * @Query("SELECT c FROM HistoricalCandles c " +
	 * "WHERE c.instrumentKey = :instrumentKey " +
	 * "AND DATE(c.candleTime) = :dateOnly")
	 */
    // HistoricalCandles findByInstrumentKeyAndCandleTime(@Param("instrumentKey") String instrumentKey, @Param("dateOnly") LocalDate dateOnly);
    
    
//Changed temporarily    
//    @Query("SELECT hc FROM HistoricalCandles hc " +
//            "WHERE hc.instrumentKey = :instrumentKey " +
//            "AND FUNCTION('DATE', hc.candleTime) = FUNCTION('DATE', :candleTime)")
//     Optional<HistoricalCandles> findByInstrumentKeyAndCandleTime(
//             @Param("instrumentKey") String instrumentKey,
//             @Param("candleTime") LocalDateTime candleTime);
	
    
   // Optional<HistoricalCandles> findByInstrumentKeyAndCandleTime(String instrumentKey, LocalDateTime candleTime);
	
   
  //  @Query("SELECT c FROM HistoricalCandles c WHERE c.instrumentKey = :instrumentKey AND DATE(c.candleTime) = :candleTime")
   // Optional<HistoricalCandles> findByInstrumentKeyAndCandleTime(@Param("instrumentKey") String instrumentKey, @Param("candleTime") LocalDateTime candleTime);
    
    Optional<HistoricalCandles> findFirstByInstrumentKeyAndCandleTimeBeforeOrderByCandleTimeDesc(String instrumentKey, LocalDateTime date);

    Optional<HistoricalCandles> findFirstByInstrumentKeyAndCandleTimeBetweenOrderByCandleTimeDesc(String instrumentKey, LocalDateTime startDateTime, LocalDateTime endDateTime);

	
	
	//List<HistoricalCandles> findByCandleTimeBetween(LocalDate startDate, LocalDate endDate);
    //@Query("SELECT h FROM HistoricalCandles h WHERE DATE(h.candleTime) BETWEEN :startDate AND :endDate")
    //List<HistoricalCandles> findByCandleTimeBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
  
    
    
//Changed temporarily     
//    @Query("SELECT h FROM HistoricalCandles h WHERE DATE(h.candleTime) >= :startDate AND DATE(h.candleTime) <= :endDate ORDER BY h.candleTime DESC")
//    List<HistoricalCandles> findByCandleTimeBetweenOrderByCandleTimeDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    
    
    
    
    
    
    
    
    
    
    @Query("SELECT MAX(c.candleTime) FROM HistoricalCandles c")
    LocalDateTime findHighestDate();

    @Query("SELECT hc FROM HistoricalCandles hc " +
            "WHERE hc.instrumentKey = :instrumentKey " +
            "AND FUNCTION('DATE', hc.candleTime) = FUNCTION('DATE', :candleTime)")
    Optional<HistoricalCandles> findByInstrumentKeyAndCandleTime(
            @Param("instrumentKey") String instrumentKey,
            @Param("candleTime") LocalDateTime candleTime);

    @Query("SELECT h FROM HistoricalCandles h WHERE DATE(h.candleTime) >= :startDate AND DATE(h.candleTime) <= :endDate ORDER BY h.candleTime DESC")
    List<HistoricalCandles> findByCandleTimeBetweenOrderByCandleTimeDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
