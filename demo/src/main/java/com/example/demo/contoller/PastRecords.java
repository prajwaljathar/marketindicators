package com.example.demo.contoller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Instrument;
import com.example.demo.repository.HistoricalCandlesRepository;
import com.example.demo.repository.InstrumentRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@RestController
//public class PastRecords {
//
//    @Autowired
//    private HistoricalCandlesRepository historicalCandlesRepository;
//
//    @GetMapping("/pastrecords")
//    public Map<String, String> historicalData(@RequestParam String fromDate, @RequestParam String toDate) {
//        String excelFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";
//
//        Map<String, String> instrumentData = readInstrumentDataFromExcel(excelFile);
//
//        String[] instrumentKeys = instrumentData.keySet().toArray(new String[0]);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(fromDate);
//        LocalDate endDate = LocalDate.parse(toDate);
//
//        Map<String, String> percentageChanges = new HashMap<>();
//
//        for (String instrumentKey : instrumentKeys) {
//            try {
//                String formattedStartDate = startDate.format(formatter);
//                String formattedEndDate = endDate.format(formatter);
//
//                String startCandleData = getCandleDataFromAPI(instrumentKey, formattedStartDate);
//                String endCandleData = getCandleDataFromAPI(instrumentKey, formattedEndDate);
//
//                if (startCandleData == null || startCandleData.equals("{}") || endCandleData == null || endCandleData.equals("{}")) {
//                    continue;
//                }
//
//                BigDecimal startOpenPrice = getOpenPriceFromCandleData(startCandleData);
//                BigDecimal endClosePrice = getClosePriceFromCandleData(endCandleData);
//
//                if (startOpenPrice != null && endClosePrice != null) {
//                    BigDecimal percentageChange = calculatePercentageChange(startOpenPrice, endClosePrice);
//                    percentageChanges.put(instrumentKey, percentageChange.toString() + "%");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return percentageChanges;
//    }
//
//    private Map<String, String> readInstrumentDataFromExcel(String excelFile) {
//        Map<String, String> instrumentData = new HashMap<>();
//        try (InputStream fis = new FileInputStream(excelFile);
//             Workbook workbook = new HSSFWorkbook(fis)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                Cell instrumentTypeCell = row.getCell(9);
//                Cell instrumentKeyCell = row.getCell(0);
//                Cell instrumentNameCell = row.getCell(3);
//
//                if (instrumentTypeCell != null && instrumentTypeCell.getCellType() == CellType.STRING
//                        && instrumentTypeCell.getStringCellValue().equals("EQUITY") && instrumentKeyCell != null
//                        && instrumentNameCell != null) {
//                    String instrumentKey = instrumentKeyCell.getStringCellValue();
//                    String instrumentName = null;
//                    if (instrumentNameCell.getCellType() == CellType.STRING) {
//                        instrumentName = instrumentNameCell.getStringCellValue();
//                    } else if (instrumentNameCell.getCellType() == CellType.NUMERIC) {
//                        instrumentName = String.valueOf(instrumentNameCell.getNumericCellValue());
//                    }
//                    if (instrumentName != null) {
//                        instrumentData.put(instrumentKey, instrumentName);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return instrumentData;
//    }
//
//    private String getCandleDataFromAPI(String instrumentKey, String date) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://api.upstox.com/v2/historical-candle/" + instrumentKey + "/day/" + date + "/" + date;
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    private BigDecimal getOpenPriceFromCandleData(String candleData) throws JSONException {
//        JSONObject jsonData = new JSONObject(candleData);
//        JSONObject data = jsonData.getJSONObject("data");
//        JSONArray candlesArray = data.getJSONArray("candles");
//
//        if (candlesArray.length() > 0) {
//            JSONArray candle = candlesArray.getJSONArray(0);
//            return candle.getBigDecimal(1);
//        }
//        return null;
//    }
//
//    private BigDecimal getClosePriceFromCandleData(String candleData) throws JSONException {
//        JSONObject jsonData = new JSONObject(candleData);
//        JSONObject data = jsonData.getJSONObject("data");
//        JSONArray candlesArray = data.getJSONArray("candles");
//
//        if (candlesArray.length() > 0) {
//            JSONArray candle = candlesArray.getJSONArray(0);
//            return candle.getBigDecimal(4);
//        }
//        return null;
//    }
//
//    private BigDecimal calculatePercentageChange(BigDecimal startOpenPrice, BigDecimal endClosePrice) {
//        return endClosePrice.subtract(startOpenPrice)
//                .divide(startOpenPrice, 4, RoundingMode.HALF_UP)
//                .multiply(BigDecimal.valueOf(100));
//    }
//}
//
//
//

//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//public class PastRecords {
//
//    @Autowired
//    private HistoricalCandlesRepository historicalCandlesRepository;
//
//    @Autowired
//    private InstrumentRepository instrumentRepository;
//
//    @GetMapping("/pastrecords")
//    public Map<String, String> historicalData(@RequestParam String fromDate, @RequestParam String toDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(fromDate);
//        LocalDate endDate = LocalDate.parse(toDate);
//
//        Map<String, String> percentageChanges = new HashMap<>();
//        List<String> processedKeys = new ArrayList<>(); // Keep track of processed keys
//
//        for (Instrument instrument : instrumentRepository.findAll()) {
//            String instrumentKey = instrument.getInstrumentKey();
//            if (processedKeys.size() >= 5) {
//                break; // Exit the loop once 5 records are processed
//            }
//
//            try {
//                String formattedStartDate = startDate.format(formatter);
//                String formattedEndDate = endDate.format(formatter);
//
//                // Fetch data for the start and end dates
//                String startCandleData = getCandleDataFromAPI(instrumentKey, formattedStartDate);
//                String endCandleData = getCandleDataFromAPI(instrumentKey, formattedEndDate);
//
//                if (startCandleData == null || startCandleData.equals("{}") || endCandleData == null || endCandleData.equals("{}")) {
//                    continue;
//                }
//
//                // Extract closing prices for the given dates
//                BigDecimal startClosePrice = getClosePriceFromCandleData(startCandleData);
//                BigDecimal endClosePrice = getClosePriceFromCandleData(endCandleData);
//
//                if (startClosePrice != null && endClosePrice != null) {
//                    // Calculate percentage change
//                    BigDecimal percentageChange = calculatePercentageChange(startClosePrice, endClosePrice);
//                    String instrumentName = instrument.getInstrumentName();
//                    percentageChanges.put(instrumentName, percentageChange.toString() + "%");
//                    processedKeys.add(instrumentKey); // Add to the list of processed keys
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return percentageChanges;
//    }
//
//    private String getCandleDataFromAPI(String instrumentKey, String date) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://api.upstox.com/v2/historical-candle/" + instrumentKey + "/day/" + date + "/" + date;
//            return restTemplate.getForObject(url, String.class);
//        } catch (RestClientException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private BigDecimal getClosePriceFromCandleData(String candleData) throws JSONException {
//        JSONObject jsonData = new JSONObject(candleData);
//        JSONObject data = jsonData.getJSONObject("data");
//        JSONArray candlesArray = data.getJSONArray("candles");
//
//        if (candlesArray.length() > 0) {
//            JSONArray candle = candlesArray.getJSONArray(0);
//            return candle.getBigDecimal(4);  // Get the closing price
//        }
//        return null;
//    }
//
//    private BigDecimal calculatePercentageChange(BigDecimal startClosePrice, BigDecimal endClosePrice) {
//        return endClosePrice.subtract(startClosePrice)
//                .divide(startClosePrice, 2, RoundingMode.HALF_UP)  // Changed to 2 decimal places
//                .multiply(BigDecimal.valueOf(100));
//    }
//}






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//public class PastRecords {
//
//    private static final Logger logger = LoggerFactory.getLogger(PastRecords.class);
//
//    @Autowired
//    private HistoricalCandlesRepository historicalCandlesRepository;
//
//    @Autowired
//    private InstrumentRepository instrumentRepository;
//
//    @GetMapping("/pastrecords")
//    public Map<String, String> historicalData(@RequestParam String fromDate, @RequestParam String toDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(fromDate);
//        LocalDate endDate = LocalDate.parse(toDate);
//
//        Map<String, String> percentageChanges = new ConcurrentHashMap<>();
//        List<Instrument> instruments = instrumentRepository.findAll();
//
//        int availableProcessors = Runtime.getRuntime().availableProcessors();
//        logger.info("AvailableProcessors"+availableProcessors);
//        int THREAD_POOL_SIZE = Math.min(availableProcessors * 2, 50);
//        logger.info("THREAD_POOL_SIZE"+THREAD_POOL_SIZE);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//
//        long startTime = System.currentTimeMillis();
//
//        List<Callable<Void>> tasks = new ArrayList<>();
//
//        for (Instrument instrument : instruments) {
//            String instrumentKey = instrument.getInstrumentKey();
//            String formattedStartDate = startDate.format(formatter);
//            String formattedEndDate = endDate.format(formatter);
//            String instrumentName = instrument.getInstrumentName();
//
//            tasks.add(() -> {
//                try {
//                    String startCandleData = getCandleDataFromAPI(instrumentKey, formattedStartDate);
//                    String endCandleData = getCandleDataFromAPI(instrumentKey, formattedEndDate);
//
//                    if (startCandleData == null || startCandleData.equals("{}") || endCandleData == null || endCandleData.equals("{}")) {
//                        return null;
//                    }
//
//                    BigDecimal startClosePrice = getClosePriceFromCandleData(startCandleData);
//                    BigDecimal endClosePrice = getClosePriceFromCandleData(endCandleData);
//
//                    if (startClosePrice != null && endClosePrice != null) {
//                        BigDecimal percentageChange = calculatePercentageChange(startClosePrice, endClosePrice);
//                        percentageChanges.put(instrumentName, percentageChange.toString() + "%");
//                    }
//                } catch (JSONException e) {
//                    logger.error("JSON parsing error for instrument: " + instrumentName, e);
//                } catch (Exception e) {
//                    logger.error("Error processing instrument: " + instrumentName, e);
//                }
//                return null;
//            });
//        }
//
//        try {
//            List<Future<Void>> futures = executorService.invokeAll(tasks);
//
//            for (Future<Void> future : futures) {
//                try {
//                    future.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    logger.error("Error executing task", e);
//                }
//            }
//        } catch (InterruptedException e) {
//            logger.error("Thread pool interrupted", e);
//        } finally {
//            executorService.shutdown();
//        }
//
//        long endTime = System.currentTimeMillis();
//        logResourceUsage(startTime, endTime, THREAD_POOL_SIZE);
//
//        return percentageChanges;
//    }
//
//    private void logResourceUsage(long startTime, long endTime, int threadPoolSize) {
//        Runtime runtime = Runtime.getRuntime();
//        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
//        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//
//        long totalMemory = runtime.totalMemory();
//        long freeMemory = runtime.freeMemory();
//        long usedMemory = totalMemory - freeMemory;
//
//        logger.info("Execution Time: {} ms", (endTime - startTime));
//        logger.info("Thread Pool Size: {}", threadPoolSize);
//        logger.info("Total Memory: {} MB", totalMemory / (1024 * 1024));
//        logger.info("Used Memory: {} MB", usedMemory / (1024 * 1024));
//        logger.info("Free Memory: {} MB", freeMemory / (1024 * 1024));
//        logger.info("Total Threads: {}", threadBean.getThreadCount());
//        logger.info("Peak Threads: {}", threadBean.getPeakThreadCount());
//        logger.info("Daemon Threads: {}", threadBean.getDaemonThreadCount());
//    }
//
//    private String getCandleDataFromAPI(String instrumentKey, String date) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://api.upstox.com/v2/historical-candle/" + instrumentKey + "/day/" + date + "/" + date;
//            return restTemplate.getForObject(url, String.class);
//        } catch (RestClientException e) {
//            logger.error("Error fetching candle data for instrument: " + instrumentKey, e);
//            return null;
//        }
//    }
//
//    private BigDecimal getClosePriceFromCandleData(String candleData) throws JSONException {
//        JSONObject jsonData = new JSONObject(candleData);
//        JSONObject data = jsonData.getJSONObject("data");
//        JSONArray candlesArray = data.getJSONArray("candles");
//
//        if (candlesArray.length() > 0) {
//            JSONArray candle = candlesArray.getJSONArray(0);
//            double closePrice = candle.getDouble(4);  // Get the closing price as double
//            return BigDecimal.valueOf(closePrice);  // Convert to BigDecimal
//        }
//        return null;
//    }
//
//    private BigDecimal calculatePercentageChange(BigDecimal startClosePrice, BigDecimal endClosePrice) {
//        return endClosePrice.subtract(startClosePrice)
//                .divide(startClosePrice, 2, RoundingMode.HALF_UP)
//                .multiply(BigDecimal.valueOf(100));
//    }
//}





@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PastRecords {

    private static final Logger logger = LoggerFactory.getLogger(PastRecords.class);

    @Autowired
    private HistoricalCandlesRepository historicalCandlesRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @GetMapping("/pastrecords")
    public Map<String, String> historicalData(@RequestParam String fromDate, @RequestParam String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        Map<String, String> percentageChanges = new ConcurrentHashMap<>();
        List<Instrument> instruments = instrumentRepository.findAll();
        logger.info("Number of instruments fetched: " + instruments.size());

        if (instruments.isEmpty()) {
            logger.warn("No instruments found in the database.");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("AvailableProcessors: " + availableProcessors);
        int THREAD_POOL_SIZE = Math.min(availableProcessors * 2, 50);
        logger.info("THREAD_POOL_SIZE: " + THREAD_POOL_SIZE);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        long startTime = System.currentTimeMillis();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Instrument instrument : instruments) {
            String instrumentKey = instrument.getInstrumentKey();
            String formattedStartDate = startDate.format(formatter);
            String formattedEndDate = endDate.format(formatter);
            String instrumentName = instrument.getInstrumentName();

            tasks.add(() -> {
                try {
                    String startCandleData = getCandleDataFromAPI(instrumentKey, formattedStartDate);
                    String endCandleData = getCandleDataFromAPI(instrumentKey, formattedEndDate);

                    if (startCandleData == null || startCandleData.equals("{}") || endCandleData == null || endCandleData.equals("{}")) {
                        logger.warn("Empty data response for instrument: " + instrumentName);
                        return null;
                    }

                    BigDecimal startClosePrice = getClosePriceFromCandleData(startCandleData);
                    BigDecimal endClosePrice = getClosePriceFromCandleData(endCandleData);

                    if (startClosePrice != null && endClosePrice != null) {
                        BigDecimal percentageChange = calculatePercentageChange(startClosePrice, endClosePrice);
                        percentageChanges.put(instrumentName, percentageChange.toString() + "%");
                    }
                } catch (JSONException e) {
                    logger.error("JSON parsing error for instrument: " + instrumentName, e);
                } catch (Exception e) {
                    logger.error("Error processing instrument: " + instrumentName, e);
                }
                return null;
            });
        }

        try {
            List<Future<Void>> futures = executorService.invokeAll(tasks);

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error executing task", e);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Thread pool interrupted", e);
        } finally {
            executorService.shutdown();
        }

        long endTime = System.currentTimeMillis();
        logResourceUsage(startTime, endTime, THREAD_POOL_SIZE);

        return percentageChanges;
    }

    private void logResourceUsage(long startTime, long endTime, int threadPoolSize) {
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        logger.info("Execution Time: {} ms", (endTime - startTime));
        logger.info("Thread Pool Size: {}", threadPoolSize);
        logger.info("Total Memory: {} MB", totalMemory / (1024 * 1024));
        logger.info("Used Memory: {} MB", usedMemory / (1024 * 1024));
        logger.info("Free Memory: {} MB", freeMemory / (1024 * 1024));
        logger.info("Total Threads: {}", threadBean.getThreadCount());
        logger.info("Peak Threads: {}", threadBean.getPeakThreadCount());
        logger.info("Daemon Threads: {}", threadBean.getDaemonThreadCount());
    }

    private String getCandleDataFromAPI(String instrumentKey, String date) {
        final int maxRetries = 3;  // Maximum number of retries
        final long retryDelay = 1000;  // Delay between retries in milliseconds

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = "https://api.upstox.com/v2/historical-candle/" + instrumentKey + "/day/" + date + "/" + date;
                return restTemplate.getForObject(url, String.class);
            } catch (RestClientException e) {
                logger.error("Error fetching candle data for instrument: " + instrumentKey + " on attempt " + attempt, e);
                if (attempt == maxRetries) {
                    // Log the final failure after all retries
                    logger.error("Failed to fetch candle data for instrument: " + instrumentKey + " after " + maxRetries + " attempts.");
                } else {
                    try {
                        // Exponential backoff strategy
                        Thread.sleep(retryDelay * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();  // Restore interrupted status
                    }
                }
            }
        }
        return null;  // Return null if all retries fail
    }

    private BigDecimal getClosePriceFromCandleData(String candleData) throws JSONException {
        JSONObject jsonData = new JSONObject(candleData);
        JSONObject data = jsonData.getJSONObject("data");
        JSONArray candlesArray = data.getJSONArray("candles");

        if (candlesArray.length() > 0) {
            JSONArray candle = candlesArray.getJSONArray(0);
            double closePrice = candle.getDouble(4);  // Get the closing price as double
            return BigDecimal.valueOf(closePrice);  // Convert to BigDecimal
        }
        return null;
    }

    private BigDecimal calculatePercentageChange(BigDecimal startClosePrice, BigDecimal endClosePrice) {
        return endClosePrice.subtract(startClosePrice)
                .divide(startClosePrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}


















