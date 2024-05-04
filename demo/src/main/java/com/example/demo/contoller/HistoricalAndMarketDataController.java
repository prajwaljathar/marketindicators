package com.example.demo.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.example.demo.model.MarketData;
import com.example.demo.repository.MarketDataRepository;
import com.example.demo.model.HistoricalCandles;
import com.example.demo.repository.HistoricalCandlesRepository;

@RestController
public class HistoricalAndMarketDataController {

	@Autowired
	private MarketDataRepository marketDataRepository;

	@Autowired
	private HistoricalCandlesRepository historicalCandlesRepository;

	@GetMapping("/update-historical-candles")
	public String historicalData() {
		// Find the highest date in HistoricalCandles repository
		LocalDateTime highestDate = historicalCandlesRepository.findHighestDate();

		// If no data exists in HistoricalCandles, set highest date as null
		if (highestDate == null) {
			highestDate = LocalDateTime.MIN;
		}

		/*
		 * // Format highestDate to match the timestamp format DateTimeFormatter
		 * formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 */

		DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");	
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		String highestDateAsString = highestDate.format(formatter);

		// Retrieve market data for dates after the highest date
		List<MarketData> marketDataList = marketDataRepository.findByTimestampAfter(highestDateAsString);

		// Iterate over each market data
		for (MarketData marketData : marketDataList) {

			
			OffsetDateTime marketDataTimestamp = OffsetDateTime.parse(marketData.getTimestamp(), timestampFormatter);

			// Convert OffsetDateTime to LocalDateTime
			LocalDateTime localDateTime = marketDataTimestamp.toLocalDateTime();

			/*
			 * // Parse the timestamp string to LocalDateTime LocalDateTime
			 * marketDataTimestamp = LocalDateTime.parse(marketData.getTimestamp(),
			 * formatter);
			 */

			// Create a HistoricalCandles object to store the candle data
			HistoricalCandles historicalCandle = new HistoricalCandles();
			historicalCandle.setInstrumentKey(marketData.getInstrumentToken());
			historicalCandle.setIntervalType("day");
			historicalCandle.setCandleTime(localDateTime); // Assuming timestamp is LocalDateTime
			historicalCandle.setOpen(marketData.getOhlcOpen());
			historicalCandle.setHigh(marketData.getOhlcHigh());
			historicalCandle.setLow(marketData.getOhlcLow());
			historicalCandle.setClose(marketData.getOhlcClose());
			//historicalCandle.setVolume(marketData.getVolume());
			historicalCandle.setVolume(marketData.getVolume() != null ? marketData.getVolume() : 0);
			historicalCandle.setOpenInterest(marketData.getOi());

			// Save the HistoricalCandles object to the database
			historicalCandlesRepository.save(historicalCandle);
		}

		return "Historical candle data saved to the database.";
	}
}