package com.example.demo.serviceimpl;

import com.example.demo.model.Instrument;
import com.example.demo.model.HistoricalDataInRangeEntity;
import com.example.demo.model.Watchlist;
import com.example.demo.model.WatchlistEntity;
import com.example.demo.repository.InstrumentRepository;
import com.example.demo.repository.HistoricalDataInRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistServiceImpl {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private HistoricalDataInRangeRepository historicalDataInRangeRepository;

    public List<WatchlistEntity> createWatchlist() {
        List<WatchlistEntity> watchlist = new ArrayList<>();

        // Retrieve instruments from the database
        List<Instrument> instruments = instrumentRepository.findAll();

        // Loop through instruments
        for (Instrument instrument : instruments) {
            List<HistoricalDataInRangeEntity> historicalData = historicalDataInRangeRepository
                    .findByInstrumentKeyOrderByDateDesc(instrument.getInstrumentKey());

            // Check if historical data exists and meets criteria
            if (historicalData.size() >= 3) {
                double thirdLastPercentageChange = historicalData.get(2).getPercentageChange();
                if (thirdLastPercentageChange < 4 || thirdLastPercentageChange > 6) {
                    // Check if the 2nd last and last records are in the range of 4-6%
                    double secondLastPercentageChange = historicalData.get(1).getPercentageChange();
                    double lastPercentageChange = historicalData.get(0).getPercentageChange();
                    if (secondLastPercentageChange >= 4 && secondLastPercentageChange <= 6
                            && lastPercentageChange >= 4 && lastPercentageChange <= 6) {
                        // Create a Watchlist object and populate with data
                    	WatchlistEntity watchlistEntry = new WatchlistEntity();
                        watchlistEntry.setInstrumentKey(instrument.getInstrumentKey());
                        watchlistEntry.setInstrumentShortName(instrument.getInstrumentShortName());
                        watchlistEntry.setInstrumentName(instrument.getInstrumentName());
                        watchlistEntry.setLastPrice(instrument.getLastPrice());
                        watchlistEntry.setLotSize(instrument.getLotSize());
                        watchlistEntry.setInstrumentType(instrument.getInstrumentType());
                        watchlistEntry.setExchange(instrument.getExchange());
                        watchlistEntry.setHistoricalInstrumentKey(historicalData.get(0).getInstrumentKey());
                        watchlistEntry.setHistoricalDate(historicalData.get(0).getDate());
                        watchlistEntry.setHistoricalPercentageChange(historicalData.get(0).getPercentageChange());
                        watchlistEntry.setHistoricalInstrumentName(historicalData.get(0).getInstrumentName());
                        // Add the Watchlist entry to the list
                        watchlist.add(watchlistEntry);
                    }
                }
            }
        }
        return watchlist;
    }
}
