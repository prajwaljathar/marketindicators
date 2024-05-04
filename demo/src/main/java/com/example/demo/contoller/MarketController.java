 package com.example.demo.contoller;
 /* 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.http.*; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.web.client.RestTemplate; import
 * com.example.demo.model.Instrument; import
 * com.example.demo.repository.InstrumentRepository; import
 * com.example.demo.model.MarketData; import
 * com.example.demo.repository.MarketDataRepository; import
 * java.math.BigDecimal; import java.net.URI; import java.util.*; import
 * java.util.stream.Collectors;
 * 
 * @RestController public class MarketController {
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * @Autowired private MarketDataRepository marketDataRepository;
 * 
 * @Value("${upstox.api.url}") private String upstoxApiUrl;
 * 
 * @Value("${upstox.api.token}") private String upstoxApiToken;
 * 
 * @GetMapping("/marketquotesdb") public String marketQuotes() {
 * List<Instrument> instruments = instrumentRepository.findAll();
 * List<List<String>> batches =
 * splitIntoBatches(instruments.stream().map(Instrument::getInstrumentKey).
 * collect(Collectors.toList()), 500);
 * 
 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization",
 * "Bearer " + upstoxApiToken); headers.set("Accept",
 * MediaType.APPLICATION_JSON_VALUE);
 * 
 * RestTemplate restTemplate = new RestTemplate();
 * 
 * for (List<String> batch : batches) { String encodedInstrumentKeys =
 * encodeInstrumentKeys(batch); String url = upstoxApiUrl +
 * "/market-quote/quotes?instrument_key=" + encodedInstrumentKeys;
 * 
 * RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
 * URI.create(url));
 * 
 * ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity,
 * Map.class);
 * 
 * if (responseEntity.getStatusCode().is2xxSuccessful()) { Map<String, Object>
 * response = responseEntity.getBody();
 * 
 * if (response != null && response.containsKey("data")) { Map<String, Object>
 * data = (Map<String, Object>) response.get("data");
 * 
 * for (Map.Entry<String, Object> entry : data.entrySet()) { String
 * instrumentToken = ((Map<String, Object>)
 * entry.getValue()).get("instrument_token").toString(); if
 * (batch.contains(instrumentToken)) { Map<String, Object> marketQuote =
 * (Map<String, Object>) entry.getValue(); // Process market quote data
 * MarketData entity = new MarketData(); entity.setStatus((String)
 * marketQuote.get("status")); entity.setOhlcOpen(new BigDecimal((Double)
 * ((Map<String, Object>) marketQuote.get("ohlc")).get("open")));
 * entity.setOhlcHigh(new BigDecimal((Double) ((Map<String, Object>)
 * marketQuote.get("ohlc")).get("high"))); entity.setOhlcLow(new
 * BigDecimal((Double) ((Map<String, Object>)
 * marketQuote.get("ohlc")).get("low"))); entity.setOhlcClose(new
 * BigDecimal((Double) ((Map<String, Object>)
 * marketQuote.get("ohlc")).get("close"))); entity.setTimestamp((String)
 * marketQuote.get("timestamp")); entity.setInstrumentToken((String)
 * marketQuote.get("instrument_token")); entity.setSymbol((String)
 * marketQuote.get("symbol")); entity.setLastPrice(new BigDecimal((Double)
 * marketQuote.get("last_price"))); entity.setVolume((Integer)
 * marketQuote.get("volume")); entity.setAveragePrice(new BigDecimal((Double)
 * marketQuote.get("average_price"))); entity.setOi(((Double)
 * marketQuote.get("oi")).intValue()); entity.setNetChange(new
 * BigDecimal((Double) marketQuote.get("net_change")));
 * entity.setTotalBuyQuantity(((Double)
 * marketQuote.get("total_buy_quantity")).intValue());
 * entity.setTotalSellQuantity(((Double)
 * marketQuote.get("total_sell_quantity")).intValue());
 * entity.setLowerCircuitLimit(new BigDecimal((Double)
 * marketQuote.get("lower_circuit_limit"))); entity.setUpperCircuitLimit(new
 * BigDecimal((Double) marketQuote.get("upper_circuit_limit")));
 * entity.setLastTradeTime((String) marketQuote.get("last_trade_time"));
 * entity.setOiDayHigh(new BigDecimal((Double) marketQuote.get("oi_day_high")));
 * entity.setOiDayLow(new BigDecimal((Double) marketQuote.get("oi_day_low")));
 * 
 * // Handle depth Map<String, Object> depthData = (Map<String, Object>)
 * marketQuote.get("depth"); if (depthData != null) { List<Map<String, Object>>
 * depthBuy = (List<Map<String, Object>>) depthData.get("buy"); if (depthBuy !=
 * null && !depthBuy.isEmpty()) { Map<String, Object> firstBuyOrder =
 * depthBuy.get(0); entity.setDepthBuyQuantity((Integer)
 * firstBuyOrder.get("quantity")); entity.setDepthBuyPrice(new
 * BigDecimal((Double) firstBuyOrder.get("price")));
 * entity.setDepthBuyOrders(((Integer) firstBuyOrder.get("orders")).intValue());
 * }
 * 
 * List<Map<String, Object>> depthSell = (List<Map<String, Object>>)
 * depthData.get("sell"); if (depthSell != null && !depthSell.isEmpty()) {
 * Map<String, Object> firstSellOrder = depthSell.get(0);
 * entity.setDepthSellQuantity(((Integer)
 * firstSellOrder.get("quantity")).intValue()); entity.setDepthSellPrice(new
 * BigDecimal((Double) firstSellOrder.get("price")));
 * entity.setDepthSellOrders(((Integer)
 * firstSellOrder.get("orders")).intValue()); } }
 * 
 * // Save to database marketDataRepository.save(entity); } } } } else { return
 * "Failed to fetch market quotes from Upstox API"; } }
 * 
 * return "Market quotes saved successfully"; }
 * 
 * private List<List<String>> splitIntoBatches(List<String> list, int batchSize)
 * { List<List<String>> batches = new ArrayList<>(); for (int i = 0; i <
 * list.size(); i += batchSize) { batches.add(list.subList(i, Math.min(i +
 * batchSize, list.size()))); } return batches; }
 * 
 * private String encodeInstrumentKeys(List<String> keys) { return
 * String.join(",", keys).replace("|", "%7C"); } }
 */



 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.http.*;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.client.RestTemplate;
 import com.example.demo.model.Instrument;
 import com.example.demo.repository.InstrumentRepository;
 import com.example.demo.model.MarketData;
 import com.example.demo.repository.MarketDataRepository;
 import java.math.BigDecimal;
 import java.net.URI;
 import java.util.*;
 import java.util.stream.Collectors;

 @RestController
 public class MarketController {

     @Autowired
     private InstrumentRepository instrumentRepository;

     @Autowired
     private MarketDataRepository marketDataRepository;

     @Value("${upstox.api.url}")
     private String upstoxApiUrl;

     @Value("${upstox.api.token}")
     private String upstoxApiToken;

     @GetMapping("/marketquotesdb")
     public String marketQuotes() {
         List<Instrument> instruments = instrumentRepository.findAll();
         List<List<String>> batches = splitIntoBatches(instruments.stream().map(Instrument::getInstrumentKey).collect(Collectors.toList()), 500);

         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "Bearer " + upstoxApiToken);
         headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

         RestTemplate restTemplate = new RestTemplate();

         for (List<String> batch : batches) {
             String encodedInstrumentKeys = encodeInstrumentKeys(batch);
             String url = upstoxApiUrl + "/market-quote/quotes?instrument_key=" + encodedInstrumentKeys;

             RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

             ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);

             if (responseEntity.getStatusCode().is2xxSuccessful()) {
                 Map<String, Object> response = responseEntity.getBody();

                 if (response != null && response.containsKey("data")) {
                     Map<String, Object> data = (Map<String, Object>) response.get("data");

                     if (data != null) { // Add null check for 'data'
                         for (Map.Entry<String, Object> entry : data.entrySet()) {
                             Map<String, Object> marketQuote = (Map<String, Object>) entry.getValue();
                             String instrumentToken = null;
                             if (marketQuote != null) { // Add null check for 'marketQuote'
                                 instrumentToken = (String) marketQuote.get("instrument_token");
                             }
                             if (instrumentToken != null && batch.contains(instrumentToken)) { // Add null check for 'instrumentToken'
                                 // Process market quote data
                                 MarketData entity = createMarketDataFromQuote(marketQuote);
                                 if (entity != null) { // Add null check for 'entity'
                                     // Save to database
                                     marketDataRepository.save(entity);
                                 }
                             }
                         }
                     }
                 }
             } else {
                 return "Failed to fetch market quotes from Upstox API";
             }
         }

         return "Market quotes saved successfully";
     }

     private MarketData createMarketDataFromQuote(Map<String, Object> marketQuote) {
    	    if (marketQuote == null) {
    	        return null;
    	    }
    	    MarketData entity = new MarketData();
    	    entity.setStatus((String) marketQuote.get("status"));
    	    Map<String, Object> ohlcData = (Map<String, Object>) marketQuote.get("ohlc");
    	    if (ohlcData != null) { // Add null check for 'ohlcData'
    	        entity.setOhlcOpen(parseBigDecimal(ohlcData.get("open")));
    	        entity.setOhlcHigh(parseBigDecimal(ohlcData.get("high")));
    	        entity.setOhlcLow(parseBigDecimal(ohlcData.get("low")));
    	        entity.setOhlcClose(parseBigDecimal(ohlcData.get("close")));
    	    }
    	    entity.setTimestamp((String) marketQuote.get("timestamp"));
    	    entity.setInstrumentToken((String) marketQuote.get("instrument_token"));
    	    entity.setSymbol((String) marketQuote.get("symbol"));
    	    entity.setLastPrice(parseBigDecimal(marketQuote.get("last_price")));
    	    entity.setVolume(parseInteger(marketQuote.get("volume")));
    	    entity.setAveragePrice(parseBigDecimal(marketQuote.get("average_price")));
    	    entity.setOi(parseInteger(marketQuote.get("oi")));
    	    entity.setNetChange(parseBigDecimal(marketQuote.get("net_change")));
    	    entity.setTotalBuyQuantity(parseInteger(marketQuote.get("total_buy_quantity")));
    	    entity.setTotalSellQuantity(parseInteger(marketQuote.get("total_sell_quantity")));
    	    entity.setLowerCircuitLimit(parseBigDecimal(marketQuote.get("lower_circuit_limit")));
    	    entity.setUpperCircuitLimit(parseBigDecimal(marketQuote.get("upper_circuit_limit")));
    	    entity.setLastTradeTime((String) marketQuote.get("last_trade_time"));
    	    entity.setOiDayHigh(parseBigDecimal(marketQuote.get("oi_day_high")));
    	    entity.setOiDayLow(parseBigDecimal(marketQuote.get("oi_day_low")));

    	    // Handle depth
    	    Map<String, Object> depthData = (Map<String, Object>) marketQuote.get("depth");
    	    if (depthData != null) {
    	        List<Map<String, Object>> depthBuy = (List<Map<String, Object>>) depthData.get("buy");
    	        if (depthBuy != null && !depthBuy.isEmpty()) {
    	            Map<String, Object> firstBuyOrder = depthBuy.get(0);
    	            entity.setDepthBuyQuantity(parseInteger(firstBuyOrder.get("quantity")));
    	            entity.setDepthBuyPrice(parseBigDecimal(firstBuyOrder.get("price")));
    	            entity.setDepthBuyOrders(parseInteger(firstBuyOrder.get("orders")));
    	        }

    	        List<Map<String, Object>> depthSell = (List<Map<String, Object>>) depthData.get("sell");
    	        if (depthSell != null && !depthSell.isEmpty()) {
    	            Map<String, Object> firstSellOrder = depthSell.get(0);
    	            entity.setDepthSellQuantity(parseInteger(firstSellOrder.get("quantity")));
    	            entity.setDepthSellPrice(parseBigDecimal(firstSellOrder.get("price")));
    	            entity.setDepthSellOrders(parseInteger(firstSellOrder.get("orders")));
    	        }
    	    }

    	    return entity;
    	}

     private List<List<String>> splitIntoBatches(List<String> list, int batchSize) {
         List<List<String>> batches = new ArrayList<>();
         for (int i = 0; i < list.size(); i += batchSize) {
             batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
         }
         return batches;
     }

     private String encodeInstrumentKeys(List<String> keys) {
         return String.join(",", keys).replace("|", "%7C");
     }

     private BigDecimal parseBigDecimal(Object value) {
         return value instanceof Double ? new BigDecimal((Double) value) : null;
     }

     private Integer parseInteger(Object value) {
         return value instanceof Double ? ((Double) value).intValue() : null;
     }
 }
