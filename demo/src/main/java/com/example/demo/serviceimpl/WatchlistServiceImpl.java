package com.example.demo.serviceimpl;

import com.example.demo.model.Instrument;
import com.example.demo.model.HistoricalCandles;
import com.example.demo.model.HistoricalDataInRangeEntity;
import com.example.demo.model.WatchlistEntity;
import com.example.demo.repository.InstrumentRepository;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.repository.HistoricalCandlesRepository;
import com.example.demo.repository.HistoricalDataInRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
 * @Service public class WatchlistServiceImpl {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private HistoricalDataInRangeRepository
 * historicalDataInRangeRepository;
 * 
 * public List<WatchlistEntity> createWatchlist() { List<WatchlistEntity>
 * watchlist = new ArrayList<>();
 * 
 * // Retrieve instruments from the database List<Instrument> instruments =
 * instrumentRepository.findAll();
 * 
 * // Loop through instruments for (Instrument instrument : instruments) {
 * List<HistoricalDataInRangeEntity> historicalData =
 * historicalDataInRangeRepository
 * .findByInstrumentKeyOrderByDateDesc(instrument.getInstrumentKey());
 * 
 * // Check if historical data exists and meets criteria if
 * (historicalData.size() >= 3) { double thirdLastPercentageChange =
 * historicalData.get(2).getPercentageChange(); if (thirdLastPercentageChange <
 * 4 || thirdLastPercentageChange > 6) { // Check if the 2nd last and last
 * records are in the range of 4-6% double secondLastPercentageChange =
 * historicalData.get(1).getPercentageChange(); double lastPercentageChange =
 * historicalData.get(0).getPercentageChange(); if (secondLastPercentageChange
 * >= 4 && secondLastPercentageChange <= 6 && lastPercentageChange >= 4 &&
 * lastPercentageChange <= 6) { // Create a Watchlist object and populate with
 * data WatchlistEntity watchlistEntry = new WatchlistEntity();
 * watchlistEntry.setInstrumentKey(instrument.getInstrumentKey());
 * watchlistEntry.setInstrumentShortName(instrument.getInstrumentShortName());
 * watchlistEntry.setInstrumentName(instrument.getInstrumentName());
 * watchlistEntry.setLastPrice(instrument.getLastPrice());
 * watchlistEntry.setLotSize(instrument.getLotSize());
 * watchlistEntry.setInstrumentType(instrument.getInstrumentType());
 * watchlistEntry.setExchange(instrument.getExchange());
 * watchlistEntry.setHistoricalInstrumentKey(historicalData.get(0).
 * getInstrumentKey());
 * watchlistEntry.setHistoricalDate(historicalData.get(0).getDate());
 * watchlistEntry.setHistoricalPercentageChange(historicalData.get(0).
 * getPercentageChange());
 * watchlistEntry.setHistoricalInstrumentName(historicalData.get(0).
 * getInstrumentName()); // Add the Watchlist entry to the list
 * watchlist.add(watchlistEntry); } } } } return watchlist; } }
 */

@Service
public class WatchlistServiceImpl {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private HistoricalDataInRangeRepository historicalDataInRangeRepository;
    
    @Autowired
    private HistoricalCandlesRepository historicalCandlesRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    public void createAndSaveWatchlist() {
        List<WatchlistEntity> watchlist = new ArrayList<>();

        // Retrieve instruments from the database
        List<Instrument> instruments = instrumentRepository.findAll();

        // Loop through instruments
        for (Instrument instrument : instruments) {
            List<HistoricalDataInRangeEntity> historicalData = historicalDataInRangeRepository
                    .findByInstrumentKeyOrderByDateDesc(instrument.getInstrumentKey());
            System.out.println("Instrument: " + instrument.getInstrumentKey());

            // Check if historical data exists and meets criteria
            if (historicalData.size() >= 3) {
                // Sort historical data by date in descending order
                historicalData.sort(Comparator.comparing(HistoricalDataInRangeEntity::getDate).reversed());

                // Get the third last, second last, and last records
                HistoricalDataInRangeEntity thirdLastRecord = historicalData.get(historicalData.size()-3);
                HistoricalDataInRangeEntity secondLastRecord = historicalData.get(historicalData.size()-2);
                HistoricalDataInRangeEntity lastRecord = historicalData.get(historicalData.size()-1);

                double thirdLastPercentageChange = thirdLastRecord.getPercentageChange();
                if (thirdLastPercentageChange < 4 || thirdLastPercentageChange > 6) {
                    // Check if the 2nd last and last records are in the range of 4-6%
                    double secondLastPercentageChange = secondLastRecord.getPercentageChange();
                    double lastPercentageChange = lastRecord.getPercentageChange();
                    if (secondLastPercentageChange >= 4 && secondLastPercentageChange <= 6
                            && lastPercentageChange >= 4 && lastPercentageChange <= 6) {
                        // Retrieve the closing price from historical candles for the last record
                        LocalDate lastDate = lastRecord.getDate();
                        String lastInstrumentKey = lastRecord.getInstrumentKey();
                        BigDecimal lastClosingPrice = getClosingPrice(lastInstrumentKey, lastDate);

                        // Check if closing price is retrieved successfully
                        if (lastClosingPrice != null) {
                            // Create a Watchlist object and populate with data
                            WatchlistEntity watchlistEntry = new WatchlistEntity();
                            watchlistEntry.setInstrumentKey(instrument.getInstrumentKey());
                            watchlistEntry.setInstrumentShortName(instrument.getInstrumentShortName());
                            watchlistEntry.setInstrumentName(instrument.getInstrumentName());
                            watchlistEntry.setLastPrice(lastClosingPrice.doubleValue());
                            watchlistEntry.setLotSize(instrument.getLotSize());
                            watchlistEntry.setInstrumentType(instrument.getInstrumentType());
                            watchlistEntry.setExchange(instrument.getExchange());
                            watchlistEntry.setHistoricalInstrumentKey(lastRecord.getInstrumentKey());
                            watchlistEntry.setHistoricalDate(lastRecord.getDate());
                            watchlistEntry.setHistoricalPercentageChange(lastRecord.getPercentageChange());
                            watchlistEntry.setHistoricalInstrumentName(lastRecord.getInstrumentName());
                            // Add the Watchlist entry to the list
                            watchlist.add(watchlistEntry);
                            System.out.println("Added to watchlist: " + watchlistEntry);
                        }
                    }
                }
            }
        }
        // Save the generated watchlist entries
        watchlistRepository.saveAll(watchlist);
        System.out.println("Watchlist entries saved.");
    }

    private BigDecimal getClosingPrice(String instrumentKey, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        Optional<HistoricalCandles> historicalCandle = historicalCandlesRepository
                .findFirstByInstrumentKeyAndCandleTimeBetweenOrderByCandleTimeDesc(instrumentKey, startOfDay, endOfDay);

        BigDecimal closingPrice = historicalCandle.map(HistoricalCandles::getClose).orElse(null);
        System.out.println("Retrieved closing price: " + closingPrice);
        return closingPrice;
    }
}


