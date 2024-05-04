package com.example.demo.contoller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.HistoricalCandles;
import com.example.demo.repository.HistoricalCandlesRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HistoricalCandlesController {

    @Autowired
    private HistoricalCandlesRepository historicalCandlesRepository;

    @GetMapping("/historicalcandles")
    public String historicalData(@RequestParam String fromDate, @RequestParam String toDate) {
        String excelFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";

        Map<String, String> instrumentData = readInstrumentDataFromExcel(excelFile);

        String[] instrumentKeys = instrumentData.keySet().toArray(new String[0]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        for (String instrumentKey : instrumentKeys) {
            try {
                String formattedStartDate = startDate.format(formatter);
                String formattedEndDate = endDate.format(formatter);

                String candleData = getCandleDataFromAPI(instrumentKey, formattedStartDate, formattedEndDate);

                if (candleData == null || candleData.equals("{}")) {
                    // Handle empty or null response
                    continue;
                }

                JSONObject jsonData = new JSONObject(candleData);
                JSONObject data = jsonData.getJSONObject("data");
                JSONArray candlesArray = data.getJSONArray("candles");

                for (int i = 0; i < candlesArray.length(); i++) {
                    JSONArray candle = candlesArray.getJSONArray(i);
                    processCandleData(instrumentKey, candle);
                }
            } catch (JSONException e) {
                // Handle JSON parsing error
                e.printStackTrace();
            } catch (HttpClientErrorException e) {
                // Handle HTTP client error
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other exceptions
                e.printStackTrace();
            }
        }

        return "Historical candle data saved to the database.";
    }

    private Map<String, String> readInstrumentDataFromExcel(String excelFile) {
        Map<String, String> instrumentData = new HashMap<>();
        try (InputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new HSSFWorkbook(fis)) { // Use HSSFWorkbook for XLS files
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell instrumentTypeCell = row.getCell(9);
                Cell instrumentKeyCell = row.getCell(0);
                Cell instrumentNameCell = row.getCell(3);

                if (instrumentTypeCell != null && instrumentTypeCell.getCellType() == CellType.STRING
                        && instrumentTypeCell.getStringCellValue().equals("EQUITY") && instrumentKeyCell != null
                        && instrumentNameCell != null) {
                    String instrumentKey = instrumentKeyCell.getStringCellValue();
                    String instrumentName = null;
                    if (instrumentNameCell.getCellType() == CellType.STRING) {
                        instrumentName = instrumentNameCell.getStringCellValue();
                    } else if (instrumentNameCell.getCellType() == CellType.NUMERIC) {
                        instrumentName = String.valueOf(instrumentNameCell.getNumericCellValue());
                    }
                    if (instrumentName != null) {
                        instrumentData.put(instrumentKey, instrumentName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instrumentData;
    }

    private String getCandleDataFromAPI(String instrumentKey, String formattedStartDate, String formattedEndDate) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.upstox.com/v2/historical-candle/" + instrumentKey + "/day/"
                + formattedEndDate + "/" + formattedStartDate;
        return restTemplate.getForObject(url, String.class);
    }

    private void processCandleData(String instrumentKey, JSONArray candle) {
        try {
            String timestamp = candle.getString(0);
            BigDecimal open = candle.getBigDecimal(1);
            BigDecimal high = candle.getBigDecimal(2);
            BigDecimal low = candle.getBigDecimal(3);
            BigDecimal close = candle.getBigDecimal(4);
            int volume = candle.getInt(5);
            int openInterest = candle.getInt(6);

            HistoricalCandles historicalCandle = new HistoricalCandles();
            historicalCandle.setInstrumentKey(instrumentKey);
            historicalCandle.setIntervalType("day");
           // Parse the timestamp string into an OffsetDateTime object
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestamp);
            // Convert OffsetDateTime to LocalDateTime
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
            // Set the candle time using the LocalDateTime object
            historicalCandle.setCandleTime(localDateTime);
           // historicalCandle.setCandleTime(LocalDateTime.parse(timestamp));
            historicalCandle.setOpen(open);
            historicalCandle.setHigh(high);
            historicalCandle.setLow(low);
            historicalCandle.setClose(close);
            historicalCandle.setVolume(volume);
            historicalCandle.setOpenInterest(openInterest);
            historicalCandlesRepository.save(historicalCandle);

            System.out.println("Processed candle for instrument: " + instrumentKey + ", timestamp: " + timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
