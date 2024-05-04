package com.example.demo.contoller;
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
import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;
import com.example.demo.service.HistoricalDataInRangeServiceImpl;
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

@RestController
public class HistoricalDataInRangeController {

    @Autowired
    private HistoricalDataInRangeServiceImpl historicalDataInRangeServiceImpl;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @GetMapping("/historicaldata/database")
    public String generateHistoricalDataDatabase(@RequestParam String fromDate, @RequestParam String toDate) {
        List<Instrument> instruments = instrumentRepository.findAll();
        Map<String, String> instrumentData = new HashMap<>();
        for (Instrument instrument : instruments) {
            instrumentData.put(instrument.getInstrumentKey(), instrument.getInstrumentShortName());
        }

        StringBuilder responseBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate = LocalDate.parse(toDate, formatter);

        Map<String, Map<String, Double>> dateInstrumentData = new LinkedHashMap<>();

        for (String instrumentKey : instrumentData.keySet()) {
            try {
                String formattedStartDate = startDate.format(formatter);
                String formattedEndDate = endDate.format(formatter);

                String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

                kong.unirest.HttpResponse<String> response = Unirest
                        .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/"
                                + formattedEndDate + "/" + formattedStartDate)
                        .header("Accept", "application/json").asString();

                int statusCode = response.getStatus();
                String responseBody = response.getBody();

                if (statusCode == 200) {
                    JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

                    Map<String, Double> instrumentPriceData = new HashMap<>();

                    for (int i = candleData.size() - 1; i >= 0; i--) {
                        JsonArray candle = candleData.get(i).getAsJsonArray();
                        String date = candle.get(0).getAsString().split("T")[0];
                        double closePrice = candle.get(4).getAsDouble();

                        instrumentPriceData.put(date, closePrice);
                    }

                    dateInstrumentData.put(instrumentKey, instrumentPriceData);
                } else {
                    responseBuilder.append("<p>Failed to retrieve data for instrument: ").append(instrumentKey)
                            .append("</p>");
                }
            } catch (Exception e) {
                responseBuilder.append("<p>Error: ").append(e.getMessage()).append("</p>");
            }
        }

        try {
        	historicalDataInRangeServiceImpl.saveDataToDatabase(dateInstrumentData);
            responseBuilder.append("<p>Data saved to database successfully.</p>");
        } catch (Exception e) {
            responseBuilder.append("<p>Error saving data to database: ").append(e.getMessage()).append("</p>");
        }

        return responseBuilder.toString();
    }
}