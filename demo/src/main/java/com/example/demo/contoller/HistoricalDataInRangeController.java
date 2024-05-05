package com.example.demo.contoller;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import kong.unirest.Unirest;
import com.google.gson.JsonObject;
import com.example.demo.model.HistoricalDataInRangeEntity;
import com.example.demo.model.Instrument;
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

/*@RestController
//@RequestMapping("/api")
public class HistoricalDataInRangeController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private HistoricalDataInRangeRepository historicalDataInRangeRepository;

    @GetMapping("/historicaldatainrange")
    @ResponseBody
    public String historicalData(@RequestParam String fromDate, @RequestParam String toDate)
            throws UnsupportedEncodingException, EncryptedDocumentException {
        System.out.println("Fetching instrument data from the database...");
        Map<String, String> instrumentData = readInstrumentDataFromDatabase();
        System.out.println("Instrument data fetched successfully.");

        StringBuilder responseBuilder = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        System.out.println("Generating historical data table...");

        responseBuilder.append("<table border=\"1\">");

        responseBuilder.append("<tr><th>Date</th>");
        for (String instrumentKey : instrumentData.keySet()) {
            responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).append("</th>");
        }
        responseBuilder.append("</tr>");

        Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();

        for (String instrumentKey : instrumentData.keySet()) {
            try {
                String formattedStartDate = startDate.format(formatter);
                String formattedEndDate = endDate.format(formatter);
                System.out.println("Fetching historical data for instrument: " + instrumentKey);

                String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

                kong.unirest.HttpResponse<String> response = Unirest
                        .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/"
                                + formattedEndDate + "/" + formattedStartDate)
                        .header("Accept", "application/json")
                        .asString();

                int statusCode = response.getStatus();
                String responseBody = response.getBody();

                if (statusCode == 200) {
                    JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

                    double previousClose = 0.0;

                    for (int i = candleData.size() - 1; i >= 0; i--) {
                        JsonArray candle = candleData.get(i).getAsJsonArray();
                        String date = candle.get(0).getAsString().split("T")[0];
                        double closePrice = candle.get(4).getAsDouble();

                        if (previousClose != 0.0) {
                            double percentageChange = ((closePrice - previousClose) / previousClose) * 100;
                            String formattedPercentageChange = String.format("%.2f%%", percentageChange);

                            dateInstrumentData.computeIfAbsent(date, k -> new HashMap<>()).put(instrumentKey,
                                    formattedPercentageChange != null ? formattedPercentageChange : "N/A");
                        }

                        previousClose = closePrice;
                    }
                } else {
                    responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
                            .append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>");
                }
            } catch (Exception e) {
                responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
                        .append("</td><td></td><td>Error: ").append(e.getMessage()).append("</td></tr>");
            }
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String date = currentDate.format(formatter);

            responseBuilder.append("<tr><td>").append(date).append("</td>");

            for (String instrumentKey : instrumentData.keySet()) {
                String percentageChange = "N/A";
                Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
                if (instrumentDataForDate != null) {
                    percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");
                }

                String color = "";
                if (!percentageChange.equals("N/A")) {
                    double percentageChangeValue = Double.parseDouble(percentageChange.replace("%", ""));
                    if (percentageChangeValue >= 18) {
                        color = "red";
                    } else if (percentageChangeValue >= 8 && percentageChangeValue <= 12) {
                        color = "red";
                    } else if (percentageChangeValue >= 4 && percentageChangeValue <= 6) {
                        color = "red";
                    }
                }

                responseBuilder.append("<td style=\"color:").append(color).append("\">").append(percentageChange)
                        .append("</td>");
            }

            responseBuilder.append("</tr>");

            currentDate = currentDate.plusDays(1);
        }

        responseBuilder.append("</table>");

        // Writing data to the database
        writeDataToDatabase(dateInstrumentData);

        System.out.println("Historical data table generated.");

        return responseBuilder.toString();
    }

    private Map<String, String> readInstrumentDataFromDatabase() {
        Map<String, String> instrumentData = new HashMap<>();
        Iterable<Instrument> instruments = instrumentRepository.findAll();
        for (Instrument instrument : instruments) {
            instrumentData.put(instrument.getInstrumentKey(), instrument.getInstrumentName());
        }
        return instrumentData;
    }

    private void writeDataToDatabase(Map<String, Map<String, String>> dateInstrumentData) {
        for (String date : dateInstrumentData.keySet()) {
            Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
            for (String instrumentKey : instrumentDataForDate.keySet()) {
                String percentageChange = instrumentDataForDate.get(instrumentKey);
                HistoricalDataInRangeEntity historicalDataInRangeEntity = new HistoricalDataInRangeEntity();
                historicalDataInRangeEntity.setDate(LocalDate.parse(date));
                historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
                historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(percentageChange.replace("%", "")));
                historicalDataInRangeRepository.save(historicalDataInRangeEntity);
            }
        }
    }
}*/




@RestController
public class HistoricalDataInRangeController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private HistoricalDataInRangeRepository historicalDataInRangeRepository;

    @GetMapping("/historicaldatainrange")
    @ResponseBody
    public String historicalData(@RequestParam String fromDate, @RequestParam String toDate)
            throws UnsupportedEncodingException, EncryptedDocumentException {
        System.out.println("Fetching instrument data from the database...");
        Map<String, String> instrumentData = readInstrumentDataFromDatabase();
        System.out.println("Instrument data fetched successfully.");

        StringBuilder responseBuilder = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        System.out.println("Generating historical data table...");

        responseBuilder.append("<table border=\"1\">");

        responseBuilder.append("<tr><th>Date</th>");
        for (String instrumentKey : instrumentData.keySet()) {
            responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).append("</th>");
        }
        responseBuilder.append("</tr>");

        Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();

        for (String instrumentKey : instrumentData.keySet()) {
            try {
                String formattedStartDate = startDate.format(formatter);
                String formattedEndDate = endDate.format(formatter);
                System.out.println("Fetching historical data for instrument: " + instrumentKey);

                String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

                kong.unirest.HttpResponse<String> response = Unirest
                        .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/"
                                + formattedEndDate + "/" + formattedStartDate)
                        .header("Accept", "application/json")
                        .asString();

                int statusCode = response.getStatus();
                String responseBody = response.getBody();

                if (statusCode == 200) {
                    JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

                    double previousClose = 0.0;

                    for (int i = candleData.size() - 1; i >= 0; i--) {
                        JsonArray candle = candleData.get(i).getAsJsonArray();
                        String date = candle.get(0).getAsString().split("T")[0];
                        double closePrice = candle.get(4).getAsDouble();

                        if (previousClose != 0.0) {
                            double percentageChange = ((closePrice - previousClose) / previousClose) * 100;
                            String formattedPercentageChange = String.format("%.2f%%", percentageChange);

                            dateInstrumentData.computeIfAbsent(date, k -> new HashMap<>()).put(instrumentKey,
                                    formattedPercentageChange != null ? formattedPercentageChange : "N/A");
                        }

                        previousClose = closePrice;
                    }
                } else {
                    responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
                            .append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>");
                }
            } catch (Exception e) {
                responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
                        .append("</td><td></td><td>Error: ").append(e.getMessage()).append("</td></tr>");
            }
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String date = currentDate.format(formatter);

            responseBuilder.append("<tr><td>").append(date).append("</td>");

            for (String instrumentKey : instrumentData.keySet()) {
                String percentageChange = "N/A";
                Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
                if (instrumentDataForDate != null) {
                    percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");
                }

                String color = "";
                if (!percentageChange.equals("N/A")) {
                    double percentageChangeValue = Double.parseDouble(percentageChange.replace("%", ""));
                    if (percentageChangeValue >= 18) {
                        color = "red";
                    } else if (percentageChangeValue >= 8 && percentageChangeValue <= 12) {
                        color = "red";
                    } else if (percentageChangeValue >= 4 && percentageChangeValue <= 6) {
                        color = "red";
                    }
                }

                responseBuilder.append("<td style=\"color:").append(color).append("\">").append(percentageChange)
                        .append("</td>");
            }

            responseBuilder.append("</tr>");

            currentDate = currentDate.plusDays(1);
        }

        responseBuilder.append("</table>");

        // Writing data to the database
        writeDataToDatabase(dateInstrumentData);

        System.out.println("Historical data table generated.");

        return responseBuilder.toString();
    }

    private Map<String, String> readInstrumentDataFromDatabase() {
        Map<String, String> instrumentData = new HashMap<>();
        Iterable<Instrument> instruments = instrumentRepository.findAll();
        for (Instrument instrument : instruments) {
            instrumentData.put(instrument.getInstrumentKey(), instrument.getInstrumentName());
        }
        return instrumentData;
    }

    private void writeDataToDatabase(Map<String, Map<String, String>> dateInstrumentData) {
        for (String date : dateInstrumentData.keySet()) {
            Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
            for (String instrumentKey : instrumentDataForDate.keySet()) {
                String percentageChange = instrumentDataForDate.get(instrumentKey);
                
                // Retrieve the instrument name from the Instrument entity
                String instrumentName = getInstrumentName(instrumentKey);

                HistoricalDataInRangeEntity historicalDataInRangeEntity = new HistoricalDataInRangeEntity();
                historicalDataInRangeEntity.setDate(LocalDate.parse(date));
                historicalDataInRangeEntity.setInstrumentKey(instrumentKey);
                historicalDataInRangeEntity.setPercentageChange(Double.parseDouble(percentageChange.replace("%", "")));
                
                // Set the instrument name
                historicalDataInRangeEntity.setInstrumentName(instrumentName);
                
                historicalDataInRangeRepository.save(historicalDataInRangeEntity);
            }
        }
    }

    private String getInstrumentName(String instrumentKey) {
        Instrument instrument = instrumentRepository.findByInstrumentKey(instrumentKey);
        if (instrument != null) {
            return instrument.getInstrumentName();
        } else {
            return null; // or handle the case when instrument is not found
        }
    }
}