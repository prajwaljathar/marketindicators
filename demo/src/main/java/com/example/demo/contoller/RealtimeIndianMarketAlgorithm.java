//package com.example.demo.contoller;
//
//import java.util.*;
//import java.util.concurrent.*;
//import java.net.URI;
//import java.net.http.*;
//import org.json.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.example.demo.model.Instrument;
//import com.example.demo.repository.InstrumentRepository;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.function.Function;
//
//@Component 
//public class IntradayIndianMarketAlgorithm {
//	
//	
//    @Autowired
//    private InstrumentRepository instrumentRepository;
//    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3UUJTOEYiLCJqdGkiOiI2Njk1ZTlhYzVhNjcwOTYzZWMwYThkNTAiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzIxMTAwNzE2LCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MjExNjcyMDB9.3GlH_g3jsQrm67cPpOSZnoIAjL1CkKE9ZkYR18iBW-g";
//    private static final String API_URL = "https://api.upstox.com/v2/market-quote/quotes";
//    private static final int MAX_POSITIONS = 5;
//    private static final int LOOKBACK_PERIOD = 100; // Number of 1-minute candles to consider
//    private static final double MAX_RISK_PER_TRADE = 0.01; // 1% of portfolio value
//
//    private Map<String, Deque<Candle>> priceHistory = new ConcurrentHashMap<>();
//    private Map<String, Position> activePositions = new ConcurrentHashMap<>();
//    private double portfolioValue = 25000; // Initial portfolio value
//
//    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
//
//    public void startTrading(List<String> instrumentKeys) {
//        executor.scheduleAtFixedRate(() -> updateMarketData(instrumentKeys), 0, 1, TimeUnit.SECONDS);
//        executor.scheduleAtFixedRate(() -> analyzeAndTrade(instrumentKeys), 0, 5, TimeUnit.SECONDS);
//
//        // Schedule market close check
//        scheduleMarketCloseCheck();
//    }
//
//    private void updateMarketData(List<String> instrumentKeys) {
//        try {
//            Map<String, JSONObject> quotes = fetchMarketQuotes(instrumentKeys);
//            for (Map.Entry<String, JSONObject> entry : quotes.entrySet()) {
//                String instrumentKey = entry.getKey();
//                JSONObject quote = entry.getValue();
//                updatePriceHistory(instrumentKey, quote);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void analyzeAndTrade(List<String> instrumentKeys) {
//        for (String instrumentKey : instrumentKeys) {
//            if (!priceHistory.containsKey(instrumentKey) || priceHistory.get(instrumentKey).size() < LOOKBACK_PERIOD) {
//                continue;
//            }
//
//            Candle currentCandle = priceHistory.get(instrumentKey).getLast();
//            double currentPrice = currentCandle.close;
//
//            TechnicalIndicators indicators = calculateTechnicalIndicators(instrumentKey);
//
//            if (!activePositions.containsKey(instrumentKey) && activePositions.size() < MAX_POSITIONS) {
//                if (shouldBuy(indicators)) {
//                    placeBuyOrder(instrumentKey, currentPrice);
//                }
//            } else if (activePositions.containsKey(instrumentKey)) {
//                if (shouldSell(instrumentKey, indicators)) {
//                    placeSellOrder(instrumentKey, currentPrice);
//                }
//            }
//        }
//    }
//
//    private boolean shouldBuy(TechnicalIndicators indicators) {
//        return indicators.rsi < 30 &&
//               indicators.macdHistogram > 0 &&
//               indicators.bollingerPercentB < 0.2 &&
//               indicators.adxTrend > 25;
//    }
//
//    private boolean shouldSell(String instrumentKey, TechnicalIndicators indicators) {
//        Position position = activePositions.get(instrumentKey);
//        double currentPrice = priceHistory.get(instrumentKey).getLast().close;
//        double profitLoss = (currentPrice - position.entryPrice) / position.entryPrice;
//
//        return indicators.rsi > 70 ||
//               indicators.macdHistogram < 0 ||
//               indicators.bollingerPercentB > 0.8 ||
//               profitLoss > 0.02 || // 2% profit target
//               profitLoss < -0.01; // 1% stop loss
//    }
//
//    private TechnicalIndicators calculateTechnicalIndicators(String instrumentKey) {
//        Deque<Candle> candles = priceHistory.get(instrumentKey);
//        double[] closePrices = candles.stream().mapToDouble(c -> c.close).toArray();
//        double[] highPrices = candles.stream().mapToDouble(c -> c.high).toArray();
//        double[] lowPrices = candles.stream().mapToDouble(c -> c.low).toArray();
//
//        TechnicalIndicators indicators = new TechnicalIndicators();
//        indicators.rsi = calculateRSI(closePrices);
//        indicators.macdHistogram = calculateMACDHistogram(closePrices);
//        indicators.bollingerPercentB = calculateBollingerPercentB(closePrices);
//        indicators.adxTrend = calculateADX(highPrices, lowPrices, closePrices);
//
//        return indicators;
//    }
//
//    private double calculateRSI(double[] prices) {
//        double gain = 0, loss = 0;
//        for (int i = 1; i < prices.length; i++) {
//            double difference = prices[i] - prices[i - 1];
//            if (difference >= 0) gain += difference;
//            else loss -= difference;
//        }
//        if (loss == 0) return 100;
//        double relativeStrength = gain / loss;
//        return 100.0 - (100.0 / (1 + relativeStrength));
//    }
//
//    private double calculateMACDHistogram(double[] prices) {
//        double[] ema12 = calculateEMA(prices, 12);
//        double[] ema26 = calculateEMA(prices, 26);
//        double[] macdLine = new double[prices.length];
//        double[] signalLine = new double[prices.length];
//
//        for (int i = 0; i < prices.length; i++) {
//            macdLine[i] = ema12[i] - ema26[i];
//            signalLine[i] = i < 9 ? macdLine[i] : (signalLine[i - 1] * 0.8 + macdLine[i] * 0.2);
//        }
//
//        return macdLine[prices.length - 1] - signalLine[prices.length - 1];
//    }
//
//    private double[] calculateEMA(double[] prices, int period) {
//        double[] ema = new double[prices.length];
//        double multiplier = 2.0 / (period + 1);
//        ema[0] = prices[0];
//        for (int i = 1; i < prices.length; i++) {
//            ema[i] = (prices[i] - ema[i - 1]) * multiplier + ema[i - 1];
//        }
//        return ema;
//    }
//
//    private double calculateBollingerPercentB(double[] prices) {
//        DescriptiveStatistics stats = new DescriptiveStatistics(prices);
//        double mean = stats.getMean();
//        double stdDev = stats.getStandardDeviation();
//        double upperBand = mean + (2 * stdDev);
//        double lowerBand = mean - (2 * stdDev);
//        double currentPrice = prices[prices.length - 1];
//        return (currentPrice - lowerBand) / (upperBand - lowerBand);
//    }
//
//    private double calculateADX(double[] highPrices, double[] lowPrices, double[] closePrices) {
//        int period = 14;
//        double[] trueRange = new double[highPrices.length];
//        double[] directionalMovementPlus = new double[highPrices.length];
//        double[] directionalMovementMinus = new double[highPrices.length];
//
//        for (int i = 1; i < highPrices.length; i++) {
//            double highDiff = highPrices[i] - highPrices[i - 1];
//            double lowDiff = lowPrices[i - 1] - lowPrices[i];
//
//            directionalMovementPlus[i] = (highDiff > lowDiff) ? Math.max(highDiff, 0) : 0;
//            directionalMovementMinus[i] = (lowDiff > highDiff) ? Math.max(lowDiff, 0) : 0;
//
//            trueRange[i] = Math.max(highPrices[i] - lowPrices[i],
//                    Math.max(Math.abs(highPrices[i] - closePrices[i - 1]),
//                            Math.abs(lowPrices[i] - closePrices[i - 1])));
//        }
//
//        double[] smoothedTrueRange = calculateWilder(trueRange, period);
//        double[] smoothedDirectionalMovementPlus = calculateWilder(directionalMovementPlus, period);
//        double[] smoothedDirectionalMovementMinus = calculateWilder(directionalMovementMinus, period);
//
//        double[] diPlus = new double[highPrices.length];
//        double[] diMinus = new double[highPrices.length];
//        double[] dx = new double[highPrices.length];
//
//        for (int i = period; i < highPrices.length; i++) {
//            diPlus[i] = 100 * smoothedDirectionalMovementPlus[i] / smoothedTrueRange[i];
//            diMinus[i] = 100 * smoothedDirectionalMovementMinus[i] / smoothedTrueRange[i];
//            dx[i] = 100 * Math.abs(diPlus[i] - diMinus[i]) / (diPlus[i] + diMinus[i]);
//        }
//
//        return Arrays.stream(dx).skip(period).average().orElse(0);
//    }
//
//    private double[] calculateWilder(double[] data, int period) {
//        double[] result = new double[data.length];
//        double sum = 0;
//        for (int i = 0; i < period; i++) {
//            sum += data[i];
//            result[i] = sum;
//        }
//        for (int i = period; i < data.length; i++) {
//            result[i] = result[i - 1] - (result[i - 1] / period) + data[i];
//        }
//        return result;
//    }
//
//    private void placeBuyOrder(String instrumentKey, double price) {
//        double positionSize = calculatePositionSize(instrumentKey, price);
//        System.out.println("Placing buy order for " + instrumentKey + " at price " + price + " with size " + positionSize);
//        activePositions.put(instrumentKey, new Position(price, positionSize));
//        portfolioValue -= positionSize * price;
//    }
//
//    private void placeSellOrder(String instrumentKey, double price) {
//        Position position = activePositions.get(instrumentKey);
//        System.out.println("Placing sell order for " + instrumentKey + " at price " + price);
//        portfolioValue += position.size * price;
//        activePositions.remove(instrumentKey);
//    }
//
//    private double calculatePositionSize(String instrumentKey, double price) {
//        double volatility = calculateVolatility(instrumentKey);
//        double riskAmount = portfolioValue * MAX_RISK_PER_TRADE;
//        return riskAmount / (price * volatility);
//    }
//
//    private double calculateVolatility(String instrumentKey) {
//        Deque<Candle> candles = priceHistory.get(instrumentKey);
//        double[] returns = new double[candles.size() - 1];
//        Iterator<Candle> iterator = candles.iterator();
//        Candle prevCandle = iterator.next();
//        int i = 0;
//        while (iterator.hasNext()) {
//            Candle currentCandle = iterator.next();
//            returns[i++] = Math.log(currentCandle.close / prevCandle.close);
//            prevCandle = currentCandle;
//        }
//        return new DescriptiveStatistics(returns).getStandardDeviation() * Math.sqrt(252);
//    }
//
//    private void updatePriceHistory(String instrumentKey, JSONObject quote) {
//        Candle candle = new Candle(
//            quote.getDouble("last_price"),
//            quote.getJSONObject("ohlc").getDouble("high"),
//            quote.getJSONObject("ohlc").getDouble("low"),
//            quote.getJSONObject("ohlc").getDouble("close"),
//            quote.getLong("last_trade_time")
//        );
//        priceHistory.computeIfAbsent(instrumentKey, k -> new LinkedList<>()).addLast(candle);
//        if (priceHistory.get(instrumentKey).size() > LOOKBACK_PERIOD) {
//            priceHistory.get(instrumentKey).removeFirst();
//        }
//    }
//
////    private Map<String, JSONObject> fetchMarketQuotes(List<String> instrumentKeys) throws Exception {
////        String encodedKeys = String.join(",", instrumentKeys);
////        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
////        HttpRequest request = HttpRequest.newBuilder()
////                .uri(URI.create(API_URL + "?instrument_key=" + encodedKeys))
////                .header("Authorization", "Bearer " + ACCESS_TOKEN)
////                .header("Accept", "application/json")
////                .GET()
////                .build();
////
////        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
////        JSONObject jsonResponse = new JSONObject(response.body());
////        JSONObject data = jsonResponse.getJSONObject("data");
////
////        Map<String, JSONObject> quotes = new HashMap<>();
////        for (String key : data.keySet()) {
////            quotes.put(key, data.getJSONObject(key));
////        }
////        return quotes;
////    }
//    
//    
//    private Map<String, JSONObject> fetchMarketQuotes(List<String> instrumentKeys) throws Exception {
//        String encodedKeys = String.join(",", instrumentKeys.stream()
//            .map(key -> {
//                try {
//                    return URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
//            })
//            .toArray(String[]::new));
//
//        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(API_URL + "?instrument_key=" + encodedKeys))
//                .header("Authorization", "Bearer " + ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        JSONObject jsonResponse = new JSONObject(response.body());
//        JSONObject data = jsonResponse.getJSONObject("data");
//
//        Map<String, JSONObject> quotes = new HashMap<>();
//        for (String key : data.keySet()) {
//            quotes.put(key, data.getJSONObject(key));
//        }
//        return quotes;
//    }
//
//
//    private void scheduleMarketCloseCheck() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime marketClose = now.withHour(15).withMinute(30).withSecond(0);
//        if (now.isAfter(marketClose)) {
//            marketClose = marketClose.plusDays(1);
//        }
//        long delay = ChronoUnit.MILLIS.between(now, marketClose);
//        
//        executor.schedule(this::closeAllPositions, delay, TimeUnit.MILLISECONDS);
//    }
//
//    private void closeAllPositions() {
//        for (Map.Entry<String, Position> entry : activePositions.entrySet()) {
//            String instrumentKey = entry.getKey();
//            double currentPrice = priceHistory.get(instrumentKey).getLast().close;
//            placeSellOrder(instrumentKey, currentPrice);
//        }
//        System.out.println("Market closed. All positions have been closed.");
//        executor.shutdown();
//    }
//
//    private static class Candle {
//        double close, high, low, open;
//        long timestamp;
//
//        Candle(double close, double high, double low, double open, long timestamp) {
//            this.close = close;
//            this.high = high;
//            this.low = low;
//            this.open = open;
//            this.timestamp = timestamp;
//        }
//    }
//
//    private static class Position {
//        double entryPrice;
//        double size;
//
//        Position(double entryPrice, double size) {
//            this.entryPrice = entryPrice;
//            this.size = size;
//        }
//    }
//
//    private static class TechnicalIndicators {
//        double rsi, macdHistogram, bollingerPercentB, adxTrend;
//    }
//}
//
//
//
package com.example.demo.contoller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.*;
import org.springframework.stereotype.Component;

@Component
public class RealtimeIndianMarketAlgorithm {

    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3UUJTOEYiLCJqdGkiOiI2Njk1ZTlhYzVhNjcwOTYzZWMwYThkNTAiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzIxMTAwNzE2LCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MjExNjcyMDB9.3GlH_g3jsQrm67cPpOSZnoIAjL1CkKE9ZkYR18iBW-g"; // Replace with your actual access token
    private static final String QUOTE_API_URL = "https://api.upstox.com/v2/market-quote/quotes";
    private static final String ORDER_BOOK_API_URL = "https://api.upstox.com/v2/order/retrieve-all";
    private static final int MAX_INSTRUMENT_KEYS_PER_REQUEST = 50;
    private static final int MAX_POSITIONS = 5;

    private Map<String, Double> lastPrice = new HashMap<>();
    private Map<String, OrderBookData> orderBooks = new HashMap<>();
    private Map<String, Position> activePositions = new HashMap<>();
    private double portfolioValue = 25000; // Initial portfolio value

    // Rate limiting variables
    private final int REQUESTS_PER_SECOND = 25;
    private final int REQUESTS_PER_MINUTE = 250;
    private final int REQUESTS_PER_30_MINUTES = 1000;
    private final Queue<Long> requestTimestamps = new LinkedList<>();
    private static final int BATCH_SIZE = 10;

    public void startTrading(List<String> allInstrumentKeys) {
        while (true) {
            try {
                for (int i = 0; i < allInstrumentKeys.size(); i += BATCH_SIZE) {
                    List<String> batchInstrumentKeys = allInstrumentKeys.subList(i, Math.min(i + BATCH_SIZE, allInstrumentKeys.size()));
                    processBatch(batchInstrumentKeys);
                }
                Thread.sleep(60000); // Wait for 1 minute before starting the next cycle
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processBatch(List<String> instrumentKeys) throws Exception {
        updateMarketData(instrumentKeys);
        analyzeAndTrade(instrumentKeys);
    }

//    private void updateMarketData(List<String> instrumentKeys) throws Exception {
//        Map<String, JSONObject> quotes = fetchMarketQuotes(instrumentKeys);
//        Map<String, JSONObject> orderBookData = fetchOrderBooks();
//
//        for (String instrumentKey : instrumentKeys) {
//            if (quotes.containsKey(instrumentKey)) {
//                double price = quotes.get(instrumentKey).getDouble("last_price");
//                lastPrice.put(instrumentKey, price);
//            }
//            if (orderBookData.containsKey(instrumentKey)) {
//                OrderBookData orderBook = parseOrderBook(orderBookData.get(instrumentKey));
//                orderBooks.put(instrumentKey, orderBook);
//            }
//        }
//    }
    
    private void updateMarketData(List<String> instrumentKeys) throws Exception {
        // Fetch market quotes for the given instrument keys
        Map<String, JSONObject> quotes = fetchMarketQuotes(instrumentKeys);
        
        // Fetch order books for the given instrument keys
        Map<String, OrderBookData> orderBookData = fetchOrderBooks(instrumentKeys);

        for (String instrumentKey : instrumentKeys) {
            if (quotes.containsKey(instrumentKey)) {
                // Extract the last price from the market quotes
                double price = quotes.get(instrumentKey).getDouble("last_price");
                lastPrice.put(instrumentKey, price);
            }
            if (orderBookData.containsKey(instrumentKey)) {
                // Get the order book data and add it to the orderBooks map
                OrderBookData orderBook = orderBookData.get(instrumentKey);
                orderBooks.put(instrumentKey, orderBook);
            }
        }
    }


    private void analyzeAndTrade(List<String> instrumentKeys) {
        for (String instrumentKey : instrumentKeys) {
            if (!lastPrice.containsKey(instrumentKey) || !orderBooks.containsKey(instrumentKey)) {
                continue;
            }

            double currentPrice = lastPrice.get(instrumentKey);
            OrderBookData orderBook = orderBooks.get(instrumentKey);

            if (!activePositions.containsKey(instrumentKey) && activePositions.size() < MAX_POSITIONS) {
                if (shouldBuy(currentPrice, orderBook)) {
                    placeBuyOrder(instrumentKey, currentPrice);
                }
            } else if (activePositions.containsKey(instrumentKey)) {
                if (shouldSell(instrumentKey, currentPrice, orderBook)) {
                    placeSellOrder(instrumentKey, currentPrice);
                }
            }
        }
    }

    private boolean shouldBuy(double currentPrice, OrderBookData orderBook) {
        double bidAskSpread = orderBook.askPrice - orderBook.bidPrice;
        double midPrice = (orderBook.askPrice + orderBook.bidPrice) / 2;

        boolean condition1 = orderBook.bidVolume > orderBook.askVolume * 1.5;
        boolean condition2 = currentPrice < midPrice;
        boolean condition3 = bidAskSpread < currentPrice * 0.001;

        return condition1 && condition2 && condition3;
    }

    private boolean shouldSell(String instrumentKey, double currentPrice, OrderBookData orderBook) {
        Position position = activePositions.get(instrumentKey);
        double profitLoss = (currentPrice - position.entryPrice) / position.entryPrice;
        double midPrice = (orderBook.askPrice + orderBook.bidPrice) / 2;

        return (orderBook.askVolume > orderBook.bidVolume * 1.5 && currentPrice > midPrice) || 
               profitLoss > 0.01 || profitLoss < -0.005;
    }

    private void placeBuyOrder(String instrumentKey, double price) {
        if (activePositions.size() >= MAX_POSITIONS) {
            return;
        }

        double positionSize = calculatePositionSize(price);
        activePositions.put(instrumentKey, new Position(price, positionSize));
        portfolioValue -= positionSize * price;
    }

    private void placeSellOrder(String instrumentKey, double price) {
        Position position = activePositions.get(instrumentKey);
        if (position == null) {
            return;
        }

        portfolioValue += position.size * price;
        activePositions.remove(instrumentKey);
    }

    private Map<String, JSONObject> fetchMarketQuotes(List<String> instrumentKeys) throws Exception {
        Map<String, JSONObject> quotes = new HashMap<>();
        int totalKeys = instrumentKeys.size();

        for (int i = 0; i < totalKeys; i += MAX_INSTRUMENT_KEYS_PER_REQUEST) {
            List<String> subList = instrumentKeys.subList(i, Math.min(totalKeys, i + MAX_INSTRUMENT_KEYS_PER_REQUEST));
            String encodedKeys = encodeInstrumentKeys(subList);
            
            String url = QUOTE_API_URL + "?instrument_key=" + encodedKeys;

            waitForRateLimit();

            HttpResponse<String> response = Unirest.get(url)
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .header("Accept", "application/json")
                    .asString();

            if (response.getStatus() == 200) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONObject data = jsonResponse.getJSONObject("data");

                for (Iterator<String> keys = data.keys(); keys.hasNext(); ) {
                    String key = keys.next();
                    JSONObject quote = data.getJSONObject(key);
                    quotes.put(key, quote);
                }
            } else {
                System.out.println("Error fetching market quotes: " + response.getStatus() + " - " + response.getStatusText());
            }
        }
        return quotes;
    }
    
    
    private Map<String, OrderBookData> fetchOrderBooks(List<String> instrumentKeys) throws Exception {
        Map<String, OrderBookData> orderBooks = new HashMap<>();
        int totalKeys = instrumentKeys.size();

        for (int i = 0; i < totalKeys; i += MAX_INSTRUMENT_KEYS_PER_REQUEST) {
            List<String> subList = instrumentKeys.subList(i, Math.min(totalKeys, i + MAX_INSTRUMENT_KEYS_PER_REQUEST));
            String encodedKeys = encodeInstrumentKeys(subList);

            String url = ORDER_BOOK_API_URL + "?instrument_key=" + encodedKeys;

            waitForRateLimit();  // Handle rate limits

            HttpResponse<String> response = Unirest.get(url)
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .header("Accept", "application/json")
                    .asString();

            if (response.getStatus() == 200) {
                // Parse response body as JSON Object and extract data array
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray == null) {
                    throw new RuntimeException("Error: 'data' field is missing in the API response.");
                }

                for (int j = 0; j < dataArray.length(); j++) {
                    JSONObject orderBook = dataArray.getJSONObject(j);

                    // Extract 'depth' object with default empty objects for bids and asks
                    JSONObject depth = orderBook.optJSONObject("depth");
                    JSONArray bids = (depth != null) ? depth.optJSONArray("bids") : new JSONArray();
                    JSONArray asks = (depth != null) ? depth.optJSONArray("asks") : new JSONArray();

                    double bidPrice = (bids.length() > 0) ? bids.optJSONObject(0).optDouble("price", 0) : 0;
                    double bidVolume = (bids.length() > 0) ? bids.optJSONObject(0).optDouble("volume", 0) : 0;
                    double askPrice = (asks.length() > 0) ? asks.optJSONObject(0).optDouble("price", 0) : 0;
                    double askVolume = (asks.length() > 0) ? asks.optJSONObject(0).optDouble("volume", 0) : 0;

                    // Create OrderBookData object
                    OrderBookData orderBookData = new OrderBookData(bidPrice, bidVolume, askPrice, askVolume);
                    orderBookData.setOhlc(orderBook.optJSONObject("ohlc"));
                    orderBookData.setDepth(depth);
                    orderBookData.setTimestamp(orderBook.optString("timestamp", ""));
                    orderBookData.setInstrumentToken(orderBook.optString("instrument_token", ""));
                    orderBookData.setSymbol(orderBook.optString("symbol", ""));
                    orderBookData.setLastPrice(orderBook.optDouble("last_price", 0));
                    orderBookData.setVolume(orderBook.optDouble("volume", 0));
                    orderBookData.setAveragePrice(orderBook.optDouble("average_price", 0));
                    orderBookData.setOi(orderBook.optDouble("oi", 0));
                    orderBookData.setNetChange(orderBook.optDouble("net_change", 0));
                    orderBookData.setTotalBuyQuantity(orderBook.optDouble("total_buy_quantity", 0));
                    orderBookData.setTotalSellQuantity(orderBook.optDouble("total_sell_quantity", 0));
                    orderBookData.setLowerCircuitLimit(orderBook.optDouble("lower_circuit_limit", 0));
                    orderBookData.setUpperCircuitLimit(orderBook.optDouble("upper_circuit_limit", 0));
                    orderBookData.setLastTradeTime(orderBook.optString("last_trade_time", ""));
                    orderBookData.setOiDayHigh(orderBook.optDouble("oi_day_high", 0));
                    orderBookData.setOiDayLow(orderBook.optDouble("oi_day_low", 0));

                    // Store in map using instrument_token
                    String instrumentKey = orderBook.getString("instrument_token");
                    orderBooks.put(instrumentKey, orderBookData);
                }
            } else {
                throw new RuntimeException("Error fetching order books: " + response.getStatus() + " - " + response.getStatusText());
            }
        }
        return orderBooks;
    }




    private Map<String, JSONObject> fetchOrderBooks() throws Exception {
        Map<String, JSONObject> orderBookData = new HashMap<>();

        String url = ORDER_BOOK_API_URL;

        waitForRateLimit();

        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Accept", "application/json")
                .asString();

        if (response.getStatus() == 200) {
            JSONArray jsonArray = new JSONArray(response.getBody());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject order = jsonArray.getJSONObject(i);
                String instrumentKey = order.getString("instrument_token");
                orderBookData.put(instrumentKey, order);
            }
        } else {
            System.out.println("Error fetching order book data: " + response.getStatus() + " - " + response.getStatusText());
        }

        return orderBookData;
    }

    private String encodeInstrumentKeys(List<String> keys) {
        try {
            String joinedKeys = String.join(",", keys);
            return URLEncoder.encode(joinedKeys, StandardCharsets.UTF_8.toString())
                    .replace("%7C", "%7C") // | character remains the same in encoding
                    .replace("+", "%20"); // Space becomes %20
        } catch (Exception e) {
            throw new RuntimeException("Error encoding instrument keys", e);
        }
    }

    private OrderBookData parseOrderBook(JSONObject jsonOrderBook) {
        double bidPrice = jsonOrderBook.getDouble("price");
        double bidVolume = jsonOrderBook.getInt("quantity");
        double askPrice = jsonOrderBook.getDouble("price");
        double askVolume = jsonOrderBook.getInt("quantity");

        return new OrderBookData(bidPrice, bidVolume, askPrice, askVolume);
    }

    private double calculatePositionSize(double price) {
        double positionSize = portfolioValue * 0.1 / price; // Invest 10% of portfolio value per position
        return Math.floor(positionSize); // Adjust as needed
    }

    private void waitForRateLimit() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        synchronized (requestTimestamps) {
            requestTimestamps.offer(currentTime);

            // Remove timestamps older than 30 minutes
            while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() > TimeUnit.MINUTES.toMillis(30)) {
                requestTimestamps.poll();
            }

            // Check rate limits
            if (requestTimestamps.size() >= REQUESTS_PER_30_MINUTES) {
                long oldestTimestamp = requestTimestamps.peek();
                long sleepTime = TimeUnit.MINUTES.toMillis(30) - (currentTime - oldestTimestamp);
                Thread.sleep(sleepTime);
            } else if (requestTimestamps.size() >= REQUESTS_PER_MINUTE && currentTime - requestTimestamps.peek() <= TimeUnit.MINUTES.toMillis(1)) {
                long oldestTimestamp = requestTimestamps.peek();
                long sleepTime = TimeUnit.MINUTES.toMillis(1) - (currentTime - oldestTimestamp);
                Thread.sleep(sleepTime);
            } else if (requestTimestamps.size() >= REQUESTS_PER_SECOND && currentTime - requestTimestamps.peek() <= TimeUnit.SECONDS.toMillis(1)) {
                long oldestTimestamp = requestTimestamps.peek();
                long sleepTime = TimeUnit.SECONDS.toMillis(1) - (currentTime - oldestTimestamp);
                Thread.sleep(sleepTime);
            }
        }
    }

//    private static class OrderBookData {
//        double bidPrice;
//        double bidVolume;
//        double askPrice;
//        double askVolume;
//
//        OrderBookData(double bidPrice, double bidVolume, double askPrice, double askVolume) {
//            this.bidPrice = bidPrice;
//            this.bidVolume = bidVolume;
//            this.askPrice = askPrice;
//            this.askVolume = askVolume;
//        }
//    }
    private static class OrderBookData {
        private final double bidPrice;
        private final double bidVolume;
        private final double askPrice;
        private final double askVolume;

        // Additional fields for order book details
        private JSONObject ohlc;
        private JSONObject depth;
        private String timestamp;
        private String instrumentToken;
        private String symbol;
        private double lastPrice;
        private double volume;
        private double averagePrice;
        private double oi;
        private double netChange;
        private double totalBuyQuantity;
        private double totalSellQuantity;
        private double lowerCircuitLimit;
        private double upperCircuitLimit;
        private String lastTradeTime;
        private double oiDayHigh;
        private double oiDayLow;

        // Constructor
        public OrderBookData(double bidPrice, double bidVolume, double askPrice, double askVolume) {
            if (bidPrice < 0 || bidVolume < 0 || askPrice < 0 || askVolume < 0) {
                throw new IllegalArgumentException("Price and volume must be non-negative.");
            }
            this.bidPrice = bidPrice;
            this.bidVolume = bidVolume;
            this.askPrice = askPrice;
            this.askVolume = askVolume;
        }

        // Getters and Setters for additional fields
        public JSONObject getOhlc() {
            return ohlc;
        }

        public void setOhlc(JSONObject ohlc) {
            this.ohlc = ohlc;
        }

        public JSONObject getDepth() {
            return depth;
        }

        public void setDepth(JSONObject depth) {
            this.depth = depth;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getInstrumentToken() {
            return instrumentToken;
        }

        public void setInstrumentToken(String instrumentToken) {
            this.instrumentToken = instrumentToken;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(double lastPrice) {
            this.lastPrice = lastPrice;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }

        public double getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(double averagePrice) {
            this.averagePrice = averagePrice;
        }

        public double getOi() {
            return oi;
        }

        public void setOi(double oi) {
            this.oi = oi;
        }

        public double getNetChange() {
            return netChange;
        }

        public void setNetChange(double netChange) {
            this.netChange = netChange;
        }

        public double getTotalBuyQuantity() {
            return totalBuyQuantity;
        }

        public void setTotalBuyQuantity(double totalBuyQuantity) {
            this.totalBuyQuantity = totalBuyQuantity;
        }

        public double getTotalSellQuantity() {
            return totalSellQuantity;
        }

        public void setTotalSellQuantity(double totalSellQuantity) {
            this.totalSellQuantity = totalSellQuantity;
        }

        public double getLowerCircuitLimit() {
            return lowerCircuitLimit;
        }

        public void setLowerCircuitLimit(double lowerCircuitLimit) {
            this.lowerCircuitLimit = lowerCircuitLimit;
        }

        public double getUpperCircuitLimit() {
            return upperCircuitLimit;
        }

        public void setUpperCircuitLimit(double upperCircuitLimit) {
            this.upperCircuitLimit = upperCircuitLimit;
        }

        public String getLastTradeTime() {
            return lastTradeTime;
        }

        public void setLastTradeTime(String lastTradeTime) {
            this.lastTradeTime = lastTradeTime;
        }

        public double getOiDayHigh() {
            return oiDayHigh;
        }

        public void setOiDayHigh(double oiDayHigh) {
            this.oiDayHigh = oiDayHigh;
        }

        public double getOiDayLow() {
            return oiDayLow;
        }

        public void setOiDayLow(double oiDayLow) {
            this.oiDayLow = oiDayLow;
        }

        // Getters
        public double getBidPrice() {
            return bidPrice;
        }

        public double getBidVolume() {
            return bidVolume;
        }

        public double getAskPrice() {
            return askPrice;
        }

        public double getAskVolume() {
            return askVolume;
        }

        // toString for easier debugging
        @Override
        public String toString() {
            return String.format("OrderBookData{bidPrice=%.2f, bidVolume=%.2f, askPrice=%.2f, askVolume=%.2f, " +
                                 "timestamp='%s', instrumentToken='%s', symbol='%s', lastPrice=%.2f, volume=%.2f, " +
                                 "averagePrice=%.2f, oi=%.2f, netChange=%.2f, totalBuyQuantity=%.2f, totalSellQuantity=%.2f, " +
                                 "lowerCircuitLimit=%.2f, upperCircuitLimit=%.2f, lastTradeTime='%s', oiDayHigh=%.2f, oiDayLow=%.2f}",
                                 bidPrice, bidVolume, askPrice, askVolume, timestamp, instrumentToken, symbol, lastPrice,
                                 volume, averagePrice, oi, netChange, totalBuyQuantity, totalSellQuantity, lowerCircuitLimit,
                                 upperCircuitLimit, lastTradeTime, oiDayHigh, oiDayLow);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderBookData that = (OrderBookData) o;
            return Double.compare(that.bidPrice, bidPrice) == 0 &&
                   Double.compare(that.bidVolume, bidVolume) == 0 &&
                   Double.compare(that.askPrice, askPrice) == 0 &&
                   Double.compare(that.askVolume, askVolume) == 0 &&
                   Double.compare(that.lastPrice, lastPrice) == 0 &&
                   Double.compare(that.volume, volume) == 0 &&
                   Double.compare(that.averagePrice, averagePrice) == 0 &&
                   Double.compare(that.oi, oi) == 0 &&
                   Double.compare(that.netChange, netChange) == 0 &&
                   Double.compare(that.totalBuyQuantity, totalBuyQuantity) == 0 &&
                   Double.compare(that.totalSellQuantity, totalSellQuantity) == 0 &&
                   Double.compare(that.lowerCircuitLimit, lowerCircuitLimit) == 0 &&
                   Double.compare(that.upperCircuitLimit, upperCircuitLimit) == 0 &&
                   Double.compare(that.oiDayHigh, oiDayHigh) == 0 &&
                   Double.compare(that.oiDayLow, oiDayLow) == 0 &&
                   Objects.equals(timestamp, that.timestamp) &&
                   Objects.equals(instrumentToken, that.instrumentToken) &&
                   Objects.equals(symbol, that.symbol) &&
                   Objects.equals(lastTradeTime, that.lastTradeTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bidPrice, bidVolume, askPrice, askVolume, timestamp, instrumentToken, symbol, lastPrice,
                                volume, averagePrice, oi, netChange, totalBuyQuantity, totalSellQuantity, lowerCircuitLimit,
                                upperCircuitLimit, lastTradeTime, oiDayHigh, oiDayLow);
        }
    }


    private static class Position {
        double entryPrice;
        double size;

        Position(double entryPrice, double size) {
            this.entryPrice = entryPrice;
            this.size = size;
        }
    }
}
