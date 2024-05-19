package com.example.demo.contoller;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import kong.unirest.Unirest;
import com.google.gson.JsonObject;
import com.example.demo.CommonMethodsUtils;
import com.example.demo.model.HistoricalCandles;
import com.example.demo.model.HistoricalDataInRangeEntity;
import com.example.demo.model.Instrument;
import com.example.demo.repository.HistoricalCandlesRepository;
import com.example.demo.repository.HistoricalDataInRangeRepository;
import com.example.demo.repository.InstrumentRepository;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @RestController //@RequestMapping("/api") public class
 * HistoricalDataInRangeController {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private HistoricalDataInRangeRepository
 * historicalDataInRangeRepository;
 * 
 * @GetMapping("/historicaldatainrange")
 * 
 * @ResponseBody public String historicalData(@RequestParam String
 * fromDate, @RequestParam String toDate) throws UnsupportedEncodingException,
 * EncryptedDocumentException {
 * System.out.println("Fetching instrument data from the database...");
 * Map<String, String> instrumentData = readInstrumentDataFromDatabase();
 * System.out.println("Instrument data fetched successfully.");
 * 
 * StringBuilder responseBuilder = new StringBuilder();
 * 
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 * LocalDate startDate = LocalDate.parse(fromDate); LocalDate endDate =
 * LocalDate.parse(toDate);
 * 
 * System.out.println("Generating historical data table...");
 * 
 * responseBuilder.append("<table border=\"1\">");
 * 
 * responseBuilder.append("<tr><th>Date</th>"); for (String instrumentKey :
 * instrumentData.keySet()) {
 * responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).
 * append("</th>"); } responseBuilder.append("</tr>");
 * 
 * Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();
 * 
 * for (String instrumentKey : instrumentData.keySet()) { try { String
 * formattedStartDate = startDate.format(formatter); String formattedEndDate =
 * endDate.format(formatter);
 * System.out.println("Fetching historical data for instrument: " +
 * instrumentKey);
 * 
 * String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");
 * 
 * kong.unirest.HttpResponse<String> response = Unirest
 * .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey +
 * "/day/" + formattedEndDate + "/" + formattedStartDate) .header("Accept",
 * "application/json") .asString();
 * 
 * int statusCode = response.getStatus(); String responseBody =
 * response.getBody();
 * 
 * if (statusCode == 200) { JsonObject responseObject =
 * JsonParser.parseString(responseBody).getAsJsonObject(); JsonArray candleData
 * = responseObject.getAsJsonObject("data").getAsJsonArray("candles");
 * 
 * double previousClose = 0.0;
 * 
 * for (int i = candleData.size() - 1; i >= 0; i--) { JsonArray candle =
 * candleData.get(i).getAsJsonArray(); String date =
 * candle.get(0).getAsString().split("T")[0]; double closePrice =
 * candle.get(4).getAsDouble();
 * 
 * if (previousClose != 0.0) { double percentageChange = ((closePrice -
 * previousClose) / previousClose) * 100; String formattedPercentageChange =
 * String.format("%.2f%%", percentageChange);
 * 
 * dateInstrumentData.computeIfAbsent(date, k -> new
 * HashMap<>()).put(instrumentKey, formattedPercentageChange != null ?
 * formattedPercentageChange : "N/A"); }
 * 
 * previousClose = closePrice; } } else {
 * responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(
 * instrumentKey))
 * .append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>")
 * ; } } catch (Exception e) {
 * responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(
 * instrumentKey))
 * .append("</td><td></td><td>Error: ").append(e.getMessage()).append(
 * "</td></tr>"); } }
 * 
 * LocalDate currentDate = startDate; while (!currentDate.isAfter(endDate)) {
 * String date = currentDate.format(formatter);
 * 
 * responseBuilder.append("<tr><td>").append(date).append("</td>");
 * 
 * for (String instrumentKey : instrumentData.keySet()) { String
 * percentageChange = "N/A"; Map<String, String> instrumentDataForDate =
 * dateInstrumentData.get(date); if (instrumentDataForDate != null) {
 * percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");
 * }
 * 
 * String color = ""; if (!percentageChange.equals("N/A")) { double
 * percentageChangeValue = Double.parseDouble(percentageChange.replace("%",
 * "")); if (percentageChangeValue >= 18) { color = "red"; } else if
 * (percentageChangeValue >= 8 && percentageChangeValue <= 12) { color = "red";
 * } else if (percentageChangeValue >= 4 && percentageChangeValue <= 6) { color
 * = "red"; } }
 * 
 * responseBuilder.append("<td style=\"color:").append(color).append("\">").
 * append(percentageChange) .append("</td>"); }
 * 
 * responseBuilder.append("</tr>");
 * 
 * currentDate = currentDate.plusDays(1); }
 * 
 * responseBuilder.append("</table>");
 * 
 * // Writing data to the database writeDataToDatabase(dateInstrumentData);
 * 
 * System.out.println("Historical data table generated.");
 * 
 * return responseBuilder.toString(); }
 * 
 * private Map<String, String> readInstrumentDataFromDatabase() { Map<String,
 * String> instrumentData = new HashMap<>(); Iterable<Instrument> instruments =
 * instrumentRepository.findAll(); for (Instrument instrument : instruments) {
 * instrumentData.put(instrument.getInstrumentKey(),
 * instrument.getInstrumentName()); } return instrumentData; }
 * 
 * private void writeDataToDatabase(Map<String, Map<String, String>>
 * dateInstrumentData) { for (String date : dateInstrumentData.keySet()) {
 * Map<String, String> instrumentDataForDate = dateInstrumentData.get(date); for
 * (String instrumentKey : instrumentDataForDate.keySet()) { String
 * percentageChange = instrumentDataForDate.get(instrumentKey);
 * HistoricalDataInRangeEntity historicalDataInRangeEntity = new
 * HistoricalDataInRangeEntity();
 * historicalDataInRangeEntity.setDate(LocalDate.parse(date));
 * historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
 * historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(
 * percentageChange.replace("%", "")));
 * historicalDataInRangeRepository.save(historicalDataInRangeEntity); } } } }
 */

/*
 * 
 * @RestController public class HistoricalDataInRangeController {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private HistoricalDataInRangeRepository
 * historicalDataInRangeRepository;
 * 
 * 
 * @GetMapping("/historicaldatainrange")
 * 
 * @ResponseBody public String historicalData(@RequestParam String
 * fromDate, @RequestParam String toDate) throws UnsupportedEncodingException,
 * EncryptedDocumentException {
 * System.out.println("Fetching instrument data from the database...");
 * Map<String, String> instrumentData = readInstrumentDataFromDatabase();
 * System.out.println("Instrument data fetched successfully.");
 * 
 * StringBuilder responseBuilder = new StringBuilder();
 * 
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 * LocalDate startDate = LocalDate.parse(fromDate); LocalDate endDate =
 * LocalDate.parse(toDate);
 * 
 * System.out.println("Generating historical data table...");
 * 
 * responseBuilder.append("<table border=\"1\">");
 * 
 * responseBuilder.append("<tr><th>Date</th>"); for (String instrumentKey :
 * instrumentData.keySet()) {
 * responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).
 * append("</th>"); } responseBuilder.append("</tr>");
 * 
 * Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();
 * 
 * for (String instrumentKey : instrumentData.keySet()) { try { String
 * formattedStartDate = startDate.format(formatter); String formattedEndDate =
 * endDate.format(formatter);
 * System.out.println("Fetching historical data for instrument: " +
 * instrumentKey);
 * 
 * String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");
 * 
 * kong.unirest.HttpResponse<String> response = Unirest
 * .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey +
 * "/day/" + formattedEndDate + "/" + formattedStartDate) .header("Accept",
 * "application/json") .asString();
 * 
 * int statusCode = response.getStatus(); String responseBody =
 * response.getBody();
 * 
 * if (statusCode == 200) { JsonObject responseObject =
 * JsonParser.parseString(responseBody).getAsJsonObject(); JsonArray candleData
 * = responseObject.getAsJsonObject("data").getAsJsonArray("candles");
 * 
 * double previousClose = 0.0;
 * 
 * for (int i = candleData.size() - 1; i >= 0; i--) { JsonArray candle =
 * candleData.get(i).getAsJsonArray(); String date =
 * candle.get(0).getAsString().split("T")[0]; double closePrice =
 * candle.get(4).getAsDouble();
 * 
 * if (previousClose != 0.0) { double percentageChange = ((closePrice -
 * previousClose) / previousClose) * 100; String formattedPercentageChange =
 * String.format("%.2f%%", percentageChange);
 * 
 * dateInstrumentData.computeIfAbsent(date, k -> new
 * HashMap<>()).put(instrumentKey, formattedPercentageChange != null ?
 * formattedPercentageChange : "N/A"); }
 * 
 * previousClose = closePrice; } } else {
 * responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(
 * instrumentKey))
 * .append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>")
 * ; } } catch (Exception e) {
 * responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(
 * instrumentKey))
 * .append("</td><td></td><td>Error: ").append(e.getMessage()).append(
 * "</td></tr>"); } }
 * 
 * LocalDate currentDate = startDate; while (!currentDate.isAfter(endDate)) {
 * String date = currentDate.format(formatter);
 * 
 * responseBuilder.append("<tr><td>").append(date).append("</td>");
 * 
 * for (String instrumentKey : instrumentData.keySet()) { String
 * percentageChange = "N/A"; Map<String, String> instrumentDataForDate =
 * dateInstrumentData.get(date); if (instrumentDataForDate != null) {
 * percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");
 * }
 * 
 * String color = ""; if (!percentageChange.equals("N/A")) { double
 * percentageChangeValue = Double.parseDouble(percentageChange.replace("%",
 * "")); if (percentageChangeValue >= 18) { color = "red"; } else if
 * (percentageChangeValue >= 8 && percentageChangeValue <= 12) { color = "red";
 * } else if (percentageChangeValue >= 4 && percentageChangeValue <= 6) { color
 * = "red"; } }
 * 
 * responseBuilder.append("<td style=\"color:").append(color).append("\">").
 * append(percentageChange) .append("</td>"); }
 * 
 * responseBuilder.append("</tr>");
 * 
 * currentDate = currentDate.plusDays(1); }
 * 
 * responseBuilder.append("</table>");
 * 
 * // Writing data to the database writeDataToDatabase(dateInstrumentData);
 * 
 * System.out.println("Historical data table generated.");
 * 
 * return responseBuilder.toString(); }
 * 
 * private Map<String, String> readInstrumentDataFromDatabase() { Map<String,
 * String> instrumentData = new HashMap<>(); Iterable<Instrument> instruments =
 * instrumentRepository.findAll(); for (Instrument instrument : instruments) {
 * instrumentData.put(instrument.getInstrumentKey(),
 * instrument.getInstrumentName()); } return instrumentData; }
 * 
 * private void writeDataToDatabase(Map<String, Map<String, String>>
 * dateInstrumentData) { for (String date : dateInstrumentData.keySet()) {
 * Map<String, String> instrumentDataForDate = dateInstrumentData.get(date); for
 * (String instrumentKey : instrumentDataForDate.keySet()) { String
 * percentageChange = instrumentDataForDate.get(instrumentKey);
 * 
 * // Retrieve the instrument name from the Instrument entity String
 * instrumentName = getInstrumentName(instrumentKey);
 * 
 * HistoricalDataInRangeEntity historicalDataInRangeEntity = new
 * HistoricalDataInRangeEntity();
 * historicalDataInRangeEntity.setDate(LocalDate.parse(date));
 * historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
 * historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(
 * percentageChange.replace("%", "")));
 * 
 * // Set the instrument name
 * historicalDataInRangeEntity.setInstrumentName(instrumentName);
 * 
 * historicalDataInRangeRepository.save(historicalDataInRangeEntity); } } }
 * 
 * private String getInstrumentName(String instrumentKey) { Instrument
 * instrument = instrumentRepository.findByInstrumentKey(instrumentKey); if
 * (instrument != null) { return instrument.getInstrumentName(); } else { return
 * null; // or handle the case when instrument is not found } } }
 * 
 */

//Complete solution
/*
 * @RestController public class HistoricalDataInRangeController {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private HistoricalCandlesRepository historicalCandlesRepository;
 * 
 * 
 * @Autowired private HistoricalDataInRangeRepository
 * historicalDataInRangeRepository; DateTimeFormatter formatter =
 * DateTimeFormatter.ofPattern("yyyy-MM-dd");
 * 
 * @GetMapping("/historicaldatainrange")
 * 
 * @ResponseBody public String historicalData(@RequestParam String
 * fromDate, @RequestParam String toDate) throws UnsupportedEncodingException,
 * EncryptedDocumentException {
 * System.out.println("Fetching instrument data from the database...");
 * Map<String, String> instrumentData = readInstrumentDataFromDatabase();
 * System.out.println("Instrument data fetched successfully.");
 * 
 * StringBuilder responseBuilder = new StringBuilder();
 * 
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 * LocalDate startDate = LocalDate.parse(fromDate); LocalDate endDate =
 * LocalDate.parse(toDate);
 * 
 * System.out.println("Generating historical data table...");
 * 
 * responseBuilder.append("<table border=\"1\">");
 * 
 * responseBuilder.append("<tr><th>Date</th>"); for (String instrumentKey :
 * instrumentData.keySet()) {
 * responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).
 * append("</th>"); } responseBuilder.append("</tr>");
 * 
 * Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();
 * 
 * // Fetch historical candle data from the database
 * 
 * // List<HistoricalCandles> historicalCandles =
 * historicalCandlesRepository.findByCandleTimeBetween(startDate, endDate);
 * List<HistoricalCandles> historicalCandles =
 * historicalCandlesRepository.findByCandleTimeBetweenOrderByCandleTimeDesc(
 * startDate, endDate); int counter = 0; // Counter to limit the number of
 * instrument keys processed for (String instrumentKey :
 * instrumentData.keySet()) { try { // Initialize map to store percentage change
 * for each date Map<String, String> instrumentPercentageChanges = new
 * HashMap<>();
 * 
 * for (HistoricalCandles candle : historicalCandles) { if
 * (candle.getInstrumentKey().equals(instrumentKey)) { String date =
 * candle.getCandleTime().toLocalDate().format(formatter); BigDecimal closePrice
 * = candle.getClose();
 * 
 * // Retrieve previous close price from the database or calculate it based on
 * your requirements BigDecimal previousClosePrice =
 * getPreviousClosePrice(candle);
 * 
 * if (previousClosePrice != null) { BigDecimal priceDiff =
 * closePrice.subtract(previousClosePrice); double percentageChange =
 * (priceDiff.doubleValue() / previousClosePrice.doubleValue()) * 100; String
 * formattedPercentageChange = String.format("%.2f%%", percentageChange);
 * 
 * instrumentPercentageChanges.put(date, formattedPercentageChange); } } }
 * 
 * dateInstrumentData.put(instrumentKey, instrumentPercentageChanges);
 * System.out.println(instrumentKey+":"+instrumentPercentageChanges);
 * 
 * 
 * counter++; if (counter >= 200) { break; // Exit loop if 200 instrument keys
 * are processed }
 * 
 * 
 * } catch (Exception e) { e.printStackTrace(); // Handle exceptions... } }
 * 
 * // Generate response (table rows) based on dateInstrumentData map for
 * (LocalDate currentDate = startDate; !currentDate.isAfter(endDate);
 * currentDate = currentDate.plusDays(1)) { String date =
 * currentDate.format(formatter);
 * 
 * responseBuilder.append("<tr><td>").append(date).append("</td>");
 * 
 * for (String instrumentKey : instrumentData.keySet()) { Map<String, String>
 * instrumentPercentageChanges = dateInstrumentData.getOrDefault(instrumentKey,
 * new HashMap<>()); String percentageChange =
 * instrumentPercentageChanges.getOrDefault(date, "N/A");
 * 
 * String color = ""; if (!percentageChange.equals("N/A")) { double
 * percentageChangeValue = Double.parseDouble(percentageChange.replace("%",
 * "")); if (percentageChangeValue >= 18 || (percentageChangeValue >= 8 &&
 * percentageChangeValue <= 12) || (percentageChangeValue >= 4 &&
 * percentageChangeValue <= 6)) { color = "red"; } }
 * 
 * responseBuilder.append("<td style=\"color:").append(color).append("\">").
 * append(percentageChange) .append("</td>"); }
 * 
 * responseBuilder.append("</tr>"); }
 * 
 * responseBuilder.append("</table>");
 * 
 * // Writing data to the database writeDataToDatabase(dateInstrumentData);
 * 
 * System.out.println("Historical data table generated.");
 * 
 * // Generate response and write data to the database... // This part is not
 * implemented here as it depends on your specific logic
 * 
 * return responseBuilder.toString(); }
 * 
 * private Map<String, String> readInstrumentDataFromDatabase() { Map<String,
 * String> instrumentData = new HashMap<>(); Iterable<Instrument> instruments =
 * instrumentRepository.findAll(); for (Instrument instrument : instruments) {
 * instrumentData.put(instrument.getInstrumentKey(),
 * instrument.getInstrumentName()); } return instrumentData; }
 * 
 * private String getInstrumentName(String instrumentKey) { Instrument
 * instrument = instrumentRepository.findByInstrumentKey(instrumentKey); if
 * (instrument != null) { return instrument.getInstrumentName(); } else { return
 * null; // or handle the case when instrument is not found } }
 * 
 * // Inside your controller class private BigDecimal
 * getPreviousClosePrice(HistoricalCandles candle) { LocalDateTime previousDate
 * = candle.getCandleTime().minusDays(1); //LocalDate previousDate =
 * candle.getCandleTime().toLocalDate().minusDays(1);
 * 
 * Optional<HistoricalCandles> optionalCandle =
 * historicalCandlesRepository.findByInstrumentKeyAndCandleTime(candle.
 * getInstrumentKey(), previousDate);
 * 
 * if (optionalCandle.isPresent()) { return optionalCandle.get().getClose(); }
 * else { return null; // Or handle the case when previous close price is not
 * available } } private void writeDataToDatabase(Map<String, Map<String,
 * String>> dateInstrumentData) { for (String instrumentKey :
 * dateInstrumentData.keySet()) { Map<String, String> instrumentDataForDate =
 * dateInstrumentData.get(instrumentKey); for (String date :
 * instrumentDataForDate.keySet()) { String percentageChange =
 * instrumentDataForDate.get(date);
 * 
 * // Retrieve the instrument name from the Instrument entity String
 * instrumentName = getInstrumentName(instrumentKey);
 * 
 * HistoricalDataInRangeEntity historicalDataInRangeEntity = new
 * HistoricalDataInRangeEntity();
 * historicalDataInRangeEntity.setDate(LocalDate.parse(date));
 * historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
 * historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(
 * percentageChange.replace("%", "")));
 * 
 * // Set the instrument name
 * historicalDataInRangeEntity.setInstrumentName(instrumentName);
 * 
 * historicalDataInRangeRepository.save(historicalDataInRangeEntity); } } } }
 * 
 */

//Working code but slow 
/*
 * @RestController public class HistoricalDataInRangeController {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private HistoricalCandlesRepository historicalCandlesRepository;
 * 
 * @Autowired private HistoricalDataInRangeRepository
 * historicalDataInRangeRepository;
 * 
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 * 
 * @GetMapping("/historicaldatainrange")
 * 
 * @ResponseBody public String historicalData(@RequestParam String
 * fromDate, @RequestParam String toDate) throws UnsupportedEncodingException,
 * EncryptedDocumentException {
 * System.out.println("Fetching instrument data from the database...");
 * Map<String, String> instrumentData = readInstrumentDataFromDatabase();
 * System.out.println("Instrument data fetched successfully.");
 * 
 * StringBuilder responseBuilder = new StringBuilder();
 * 
 * LocalDate startDate = LocalDate.parse(fromDate); LocalDate endDate =
 * LocalDate.parse(toDate);
 * 
 * System.out.println("Generating historical data table...");
 * 
 * responseBuilder.append("<table border=\"1\">");
 * 
 * responseBuilder.append("<tr><th>Date</th>"); for (String instrumentKey :
 * instrumentData.keySet()) {
 * responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).
 * append("</th>"); } responseBuilder.append("</tr>");
 * 
 * Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();
 * // int counter = 0; // Counter to limit the number of instrument keys
 * processed for (String instrumentKey : instrumentData.keySet()) { try { //
 * Initialize map to store percentage change for each date Map<String, String>
 * instrumentPercentageChanges = new HashMap<>();
 * 
 * // Iterate through historical candles for (LocalDate currentDate = startDate;
 * !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
 * String date = currentDate.format(formatter);
 * 
 * // Retrieve historical candle for the current date and instrument
 * Optional<HistoricalCandles> optionalCandle =
 * historicalCandlesRepository.findByInstrumentKeyAndCandleTime(instrumentKey,
 * currentDate.atStartOfDay()); if (optionalCandle.isPresent()) {
 * HistoricalCandles candle = optionalCandle.get(); BigDecimal closePrice =
 * candle.getClose();
 * 
 * // Retrieve previous trading day's close price //BigDecimal
 * previousClosePrice = getPreviousTradingDayClosePrice(candle); BigDecimal
 * previousClosePrice = getPreviousTradingDayClosePrice(candle, startDate);
 * 
 * if (previousClosePrice != null) { BigDecimal priceDiff =
 * closePrice.subtract(previousClosePrice); double percentageChange =
 * (priceDiff.doubleValue() / previousClosePrice.doubleValue()) * 100; String
 * formattedPercentageChange = String.format("%.2f%%", percentageChange);
 * 
 * instrumentPercentageChanges.put(date, formattedPercentageChange); } } }
 * 
 * dateInstrumentData.put(instrumentKey, instrumentPercentageChanges);
 * System.out.println(instrumentKey+":"+instrumentPercentageChanges);
 * 
 * 
 * counter++; if (counter >= 200) { break; // Exit loop if 200 instrument keys
 * are processed }
 * 
 * } catch (Exception e) { e.printStackTrace(); // Handle exceptions... } }
 * 
 * // Generate response (table rows) based on dateInstrumentData map for
 * (LocalDate currentDate = startDate; !currentDate.isAfter(endDate);
 * currentDate = currentDate.plusDays(1)) { String date =
 * currentDate.format(formatter);
 * 
 * responseBuilder.append("<tr><td>").append(date).append("</td>");
 * 
 * for (String instrumentKey : instrumentData.keySet()) { Map<String, String>
 * instrumentPercentageChanges = dateInstrumentData.getOrDefault(instrumentKey,
 * new HashMap<>()); String percentageChange =
 * instrumentPercentageChanges.getOrDefault(date, "N/A");
 * 
 * String color = ""; if (!percentageChange.equals("N/A")) { double
 * percentageChangeValue = Double.parseDouble(percentageChange.replace("%",
 * "")); if (percentageChangeValue >= 18 || (percentageChangeValue >= 8 &&
 * percentageChangeValue <= 12) || (percentageChangeValue >= 4 &&
 * percentageChangeValue <= 6)) { color = "red"; } }
 * 
 * responseBuilder.append("<td style=\"color:").append(color).append("\">").
 * append(percentageChange) .append("</td>"); }
 * 
 * responseBuilder.append("</tr>"); }
 * 
 * responseBuilder.append("</table>");
 * 
 * // Writing data to the database writeDataToDatabase(dateInstrumentData);
 * 
 * System.out.println("Historical data table generated.");
 * 
 * // Generate response and write data to the database... // This part is not
 * implemented here as it depends on your specific logic
 * 
 * return responseBuilder.toString(); }
 * 
 * private Map<String, String> readInstrumentDataFromDatabase() { Map<String,
 * String> instrumentData = new HashMap<>(); Iterable<Instrument> instruments =
 * instrumentRepository.findAll(); for (Instrument instrument : instruments) {
 * instrumentData.put(instrument.getInstrumentKey(),
 * instrument.getInstrumentName()); } return instrumentData; }
 * 
 * private BigDecimal getPreviousTradingDayClosePrice(HistoricalCandles
 * currentCandle, LocalDate fromDate) { LocalDate previousDate =
 * currentCandle.getCandleTime().toLocalDate().minusDays(1);
 * 
 * // Iterate backward until finding the previous trading day's data while
 * (previousDate.isAfter(fromDate) || previousDate.isEqual(fromDate)) {
 * Optional<HistoricalCandles> optionalCandle =
 * historicalCandlesRepository.findByInstrumentKeyAndCandleTime(currentCandle.
 * getInstrumentKey(), previousDate.atStartOfDay()); if
 * (optionalCandle.isPresent()) { return optionalCandle.get().getClose(); }
 * previousDate = previousDate.minusDays(1); } return null; // If no data is
 * found before the fromDate }
 * 
 * private void writeDataToDatabase(Map<String, Map<String, String>>
 * dateInstrumentData) { for (String instrumentKey :
 * dateInstrumentData.keySet()) { Map<String, String> instrumentDataForDate =
 * dateInstrumentData.get(instrumentKey); for (String date :
 * instrumentDataForDate.keySet()) { String percentageChange =
 * instrumentDataForDate.get(date);
 * 
 * // Retrieve the instrument name from the Instrument entity String
 * instrumentName = getInstrumentName(instrumentKey);
 * 
 * HistoricalDataInRangeEntity historicalDataInRangeEntity = new
 * HistoricalDataInRangeEntity();
 * historicalDataInRangeEntity.setDate(LocalDate.parse(date));
 * historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
 * historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(
 * percentageChange.replace("%", "")));
 * 
 * // Set the instrument name
 * historicalDataInRangeEntity.setInstrumentName(instrumentName);
 * 
 * historicalDataInRangeRepository.save(historicalDataInRangeEntity); } } }
 * 
 * private String getInstrumentName(String instrumentKey) { Instrument
 * instrument = instrumentRepository.findByInstrumentKey(instrumentKey); if
 * (instrument != null) { return instrument.getInstrumentName(); } else { return
 * null; // or handle the case when instrument is not found } } }
 */

@RestController
public class HistoricalDataInRangeController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private HistoricalCandlesRepository historicalCandlesRepository;

    @Autowired
    private HistoricalDataInRangeRepository historicalDataInRangeRepository;
    @Autowired
    private  CommonMethodsUtils commonMethodsUtils;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/historicaldatainrange")
    @ResponseBody
    public String historicalData(@RequestParam String fromDate, @RequestParam String toDate)
            throws UnsupportedEncodingException, EncryptedDocumentException {
        System.out.println("Fetching instrument data from the database...");
        Map<String, String> instrumentData = commonMethodsUtils.readInstrumentDataFromDatabase();
        System.out.println("Instrument data fetched successfully.");

        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        System.out.println("Generating historical data table...");
        
        // Initialize list to batch save entities
        List<HistoricalDataInRangeEntity> entitiesToSave = new ArrayList<>();

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<table border=\"1\">");
        responseBuilder.append("<tr><th>Date</th>");
        
        for (String instrumentKey : instrumentData.keySet()) {
            responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).append("</th>");
        }
        
        responseBuilder.append("</tr>");

        // Initialize instrumentPercentageChanges outside the loop
        Map<String, Map<String, String>> instrumentPercentageChangesMap = new HashMap<>();

        for (String instrumentKey : instrumentData.keySet()) {
            try {
                System.out.println("Processing instrument: " + instrumentKey);

                Map<String, String> instrumentPercentageChanges = new HashMap<>();

                for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
                    String date = currentDate.format(formatter);

                    System.out.println("Processing date: " + date);

                    Optional<HistoricalCandles> optionalCandle = historicalCandlesRepository.findByInstrumentKeyAndCandleTime(instrumentKey, currentDate.atStartOfDay());
                    
                    if (optionalCandle.isPresent()) {
                        HistoricalCandles candle = optionalCandle.get();
                        BigDecimal closePrice = candle.getClose();
                        BigDecimal previousClosePrice = getPreviousTradingDayClosePrice(candle, startDate);

                        if (previousClosePrice != null) {
                            BigDecimal priceDiff = closePrice.subtract(previousClosePrice);
                            double percentageChange = (priceDiff.doubleValue() / previousClosePrice.doubleValue()) * 100;
                            String formattedPercentageChange = String.format("%.2f%%", percentageChange);

                            instrumentPercentageChanges.put(date, formattedPercentageChange);

                            // Create entity and add to batch save list
                            HistoricalDataInRangeEntity entity = new HistoricalDataInRangeEntity();
                            entity.setDate(LocalDate.parse(date));
                            entity.setInstrumentKey(instrumentKey);
                            entity.setPercentageChange(Double.parseDouble(formattedPercentageChange.replace("%", "")));
                            entity.setInstrumentName(instrumentData.get(instrumentKey));
                            entitiesToSave.add(entity);
                        }
                    }
                }

                // Add instrument data to map
                instrumentPercentageChangesMap.put(instrumentKey, instrumentPercentageChanges);
                System.out.println(instrumentKey + ":" + instrumentPercentageChanges);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions...
            }
        }

        // Batch save entities
        historicalDataInRangeRepository.saveAll(entitiesToSave);

        // Generate response (table rows)
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            String date = currentDate.format(formatter);

            responseBuilder.append("<tr><td>").append(date).append("</td>");

            for (String instrumentKey : instrumentData.keySet()) {
                Map<String, String> instrumentPercentageChanges = instrumentPercentageChangesMap.getOrDefault(instrumentKey, new HashMap<>());
                String percentageChange = instrumentPercentageChanges.getOrDefault(date, "N/A");

                String color = "";
                if (!percentageChange.equals("N/A")) {
                    double percentageChangeValue = Double.parseDouble(percentageChange.replace("%", ""));
                    if (percentageChangeValue >= 18 || (percentageChangeValue >= 8 && percentageChangeValue <= 12) || (percentageChangeValue >= 4 && percentageChangeValue <= 6)) {
                        color = "red";
                    }
                }

                responseBuilder.append("<td style=\"color:").append(color).append("\">").append(percentageChange)
                        .append("</td>");
            }

            responseBuilder.append("</tr>");
        }

        responseBuilder.append("</table>");

        System.out.println("Historical data table generated.");

        return responseBuilder.toString();
    }

	/*
	 * private Map<String, String> readInstrumentDataFromDatabase() { Map<String,
	 * String> instrumentData = new HashMap<>(); Iterable<Instrument> instruments =
	 * instrumentRepository.findAll(); for (Instrument instrument : instruments) {
	 * instrumentData.put(instrument.getInstrumentKey(),
	 * instrument.getInstrumentName()); } return instrumentData; }
	 */

    private BigDecimal getPreviousTradingDayClosePrice(HistoricalCandles currentCandle, LocalDate fromDate) {
        LocalDate previousDate = currentCandle.getCandleTime().toLocalDate().minusDays(1);

        while (previousDate.isAfter(fromDate) || previousDate.isEqual(fromDate)) {
            Optional<HistoricalCandles> optionalCandle = historicalCandlesRepository.findByInstrumentKeyAndCandleTime(currentCandle.getInstrumentKey(), previousDate.atStartOfDay());
            if (optionalCandle.isPresent()) {
                return optionalCandle.get().getClose();
            }
            previousDate = previousDate.minusDays(1);
        }
        return null;
    }
}