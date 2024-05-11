/*
 * package com.example.demo.contoller;
 * 
 * import java.io.File; import java.io.FileInputStream; import
 * java.io.FileOutputStream; import java.io.IOException; import
 * java.io.UnsupportedEncodingException; import java.net.InetSocketAddress;
 * import java.net.URI; import java.net.URISyntaxException; import
 * java.net.URLEncoder; import java.net.http.HttpClient; import
 * java.net.http.HttpHeaders; import java.net.http.HttpRequest; import
 * java.net.http.HttpResponse; import java.nio.ByteBuffer; import
 * java.nio.charset.StandardCharsets; import java.nio.file.Files; import
 * java.nio.file.Paths; import java.security.KeyManagementException; import
 * java.security.NoSuchAlgorithmException; import
 * java.security.cert.CertificateException; import java.time.LocalDate; import
 * java.time.format.DateTimeFormatter; import java.util.ArrayList; import
 * java.util.Arrays; import java.util.HashMap; import java.util.HashSet; import
 * java.util.LinkedHashMap; import java.util.List; import java.util.Map; import
 * java.util.Objects; import java.util.Set; import
 * java.util.concurrent.CountDownLatch; import java.util.stream.Collectors;
 * 
 * import javax.net.ssl.HttpsURLConnection; import javax.net.ssl.SSLContext;
 * import javax.net.ssl.TrustManager; import javax.net.ssl.X509TrustManager;
 * 
 * import org.apache.poi.EncryptedDocumentException; import
 * org.apache.poi.openxml4j.exceptions.InvalidFormatException; import
 * org.apache.poi.ss.usermodel.Cell; import
 * org.apache.poi.ss.usermodel.CellStyle; import
 * org.apache.poi.ss.usermodel.CellType; import
 * org.apache.poi.ss.usermodel.FillPatternType; import
 * org.apache.poi.ss.usermodel.IndexedColors; import
 * org.apache.poi.ss.usermodel.Row; import org.apache.poi.ss.usermodel.Sheet;
 * import org.apache.poi.ss.usermodel.Workbook; import
 * org.apache.poi.ss.usermodel.WorkbookFactory; import
 * org.apache.poi.xssf.usermodel.XSSFWorkbook;
 * 
 * import org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.stereotype.Controller;
 * 
 * import com.google.gson.Gson; import com.google.gson.JsonArray; import
 * com.google.gson.JsonObject; import com.google.gson.JsonParser; import
 * com.google.protobuf.InvalidProtocolBufferException; import
 * com.upstox.ApiClient; import com.upstox.ApiException; import
 * com.upstox.Configuration; import
 * com.upstox.api.WebsocketAuthRedirectResponse; import com.upstox.auth.OAuth;
 * import com.upstox.feeder.MarketDataStreamer; import
 * com.upstox.feeder.MarketUpdate; import com.upstox.feeder.constants.Mode;
 * import com.upstox.feeder.listener.OnMarketUpdateListener; import
 * com.upstox.marketdatafeeder.rpc.proto.MarketDataFeed.Feed; import
 * com.upstox.marketdatafeeder.rpc.proto.MarketDataFeed.LTPC;
 * 
 * import io.swagger.client.api.WebsocketApi;
 * 
 * import org.java_websocket.WebSocketImpl; import
 * org.java_websocket.client.WebSocketClient; import
 * org.java_websocket.handshake.ClientHandshake; import
 * org.java_websocket.handshake.ServerHandshake; import org.json.JSONObject;
 * import org.java_websocket.drafts.Draft; import
 * org.java_websocket.drafts.Draft_6455; import
 * org.java_websocket.exceptions.InvalidDataException; import
 * org.java_websocket.framing.CloseFrame; import
 * org.java_websocket.framing.Framedata; import
 * org.java_websocket.framing.PingFrame; import
 * com.google.protobuf.util.JsonFormat;
 * 
 * import okhttp3.*;
 * 
 * import com.example.demo.InstrumentKey; // Assuming this is a custom class in
 * your project import com.example.demo.model.WatchlistEntity; import
 * com.example.demo.serviceimpl.WatchlistServiceImpl;
 * 
 * import kong.unirest.Unirest;
 * 
 * @RestController public class PlaceOrderContoller {
 * 
 * 
 * private static final String ACCESS_TOKEN =
 * "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3NEFKRkwiLCJqdGkiOiI2NjI5YzkxZDM1MzM5ZTNiYjQwYTkwZDgiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsInNjb3BlIjpbImludGVyYWN0aXZlIiwiaGlzdG9yaWNhbCJdLCJpYXQiOjE3MTQwMTQ0OTMsImlzcyI6InVkYXBpLWdhdGV3YXktc2VydmljZSIsImV4cCI6MTcxNDA4MjQwMH0.JDfXykvWPkSkpGHfMY7krv8E7o-3jsy4DtmCHJY_TVg";
 * //private static List<String> instruments = new ArrayList<>(); //private
 * static final String filePath =
 * "C:\\Users\\admin\\source\\repos\\Trading\\Trading\\Instruments.xlsx";
 * private Set<String> executedBuyOrders = new HashSet<>(); // Set to store
 * executed buy orders private Map<String, Double> initialLTPMap = new
 * HashMap<>(); private Map<String, Double> buyPriceMap = new HashMap<>();// Map
 * to store initial LTP for each instrument
 * 
 * @GetMapping("/getrealtimedata") public void getRealTimeUpdate() throws
 * ApiException { // Assuming you have a service to retrieve watchlist items
 * List<WatchlistEntity> watchlist =
 * WatchlistServiceImpl.getAllWatchlistItems();
 * 
 * // Set up the instrument keys from watchlist Set<String> instrumentKeys = new
 * HashSet<>(); for (WatchlistEntity watchlistItem : watchlist) {
 * instrumentKeys.add(watchlistItem.getInstrumentKey()); }
 * 
 * // Initialize OAuth and MarketDataStreamer ApiClient defaultClient =
 * Configuration.getDefaultApiClient(); OAuth oAuth = (OAuth)
 * defaultClient.getAuthentication("OAUTH2");
 * oAuth.setAccessToken(ACCESS_TOKEN);
 * 
 * final MarketDataStreamer marketDataStreamer = new
 * MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);
 * 
 * // Set up listener for market updates
 * marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
 * 
 * @Override public void onUpdate(MarketUpdate marketUpdate) { // Iterate over
 * the feeds and extract LTP for (Map.Entry<String, MarketUpdate.Feed> entry :
 * marketUpdate.getFeeds().entrySet()) { String instrumentKey = entry.getKey();
 * MarketUpdate.Feed marketUpdateFeed = entry.getValue();
 * 
 * if (marketUpdateFeed != null && marketUpdateFeed.getFf() != null &&
 * marketUpdateFeed.getFf().getMarketFF() != null) { MarketUpdate.LTPC ltpc =
 * marketUpdateFeed.getFf().getMarketFF().getLtpc(); if (ltpc != null) { Double
 * ltp = ltpc.getLtp();
 * 
 * // Place buy order if no buy order executed yet if
 * (!executedBuyOrders.contains(instrumentKey)) { double buyPrice = ltp + 0.20;
 * // Example multiplier, change as needed
 * placeBuyOrderForInstrument(instrumentKey, buyPrice); } else { // Check for
 * sell conditions sellOrderForInstrument(instrumentKey, ltp * 0.03); // Example
 * multiplier, change as needed } } } } } });
 * 
 * // Connect to market data stream marketDataStreamer.connect(); }
 * 
 * private void placeBuyOrderForInstrument(String instrumentKey, Double
 * buyPrice) { try { String url = "https://api.upstox.com/v2/order/place";
 * String token = "Bearer " + ACCESS_TOKEN;
 * 
 * // Set up the request body for placing buy order String requestBody = "{" +
 * "\"quantity\": 1," + "\"product\": \"D\"," + "\"validity\": \"DAY\"," +
 * "\"price\": " + buyPrice + "," + "\"tag\": \"string\"," +
 * "\"instrument_token\": \"" + instrumentKey + "\"," +
 * "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," +
 * "\"disclosed_quantity\": 0," + "\"trigger_price\": " + buyPrice + "," +
 * "\"is_amo\": false" + "}";
 * 
 * // Create the HttpClient HttpClient httpClient = HttpClient.newHttpClient();
 * 
 * // Create the HttpRequest HttpRequest request =
 * HttpRequest.newBuilder().uri(URI.create(url)) .header("Content-Type",
 * "application/json").header("Accept", "application/json")
 * .header("Authorization",
 * token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
 * 
 * // Send the request and retrieve the response HttpResponse<String> response =
 * httpClient.send(request, HttpResponse.BodyHandlers.ofString());
 * 
 * // Check if the buy order was successful if (response.statusCode() == 200) {
 * // Order successfully placed, update status or take further action
 * executedBuyOrders.add(instrumentKey); // Update the set to indicate
 * successful buy order execution
 * System.out.println("Buy Order placed successfully"); } else { // Order
 * placement failed, handle accordingly
 * System.out.println("Failed to place Buy Order: " + response.body()); } }
 * catch (Exception e) { e.printStackTrace(); // Handle exceptions } }
 * 
 * private void sellOrderForInstrument(String instrumentKey, Double
 * closingPrice) { try { String url = "https://api.upstox.com/v2/order/place";
 * String token = "Bearer " + ACCESS_TOKEN;
 * 
 * // Set up the request body for selling the order String requestBody = "{" +
 * "\"instrument_token\": \"" + instrumentKey + "\"," + "\"quantity\": 1," +
 * "\"product\": \"D\"," + "\"order_type\": \"LIMIT\"," +
 * "\"transaction_type\": \"SELL\"," + "\"price\": " + closingPrice + "," +
 * "\"trigger_price\": " + closingPrice + "," + "\"validity\": \"DAY\"," +
 * "\"disclosed_quantity\": 0," + "\"is_amo\": false" + "}";
 * 
 * // Create the HttpClient HttpClient httpClient = HttpClient.newHttpClient();
 * 
 * // Create the HttpRequest HttpRequest request = HttpRequest.newBuilder()
 * .uri(URI.create(url)) .header("Content-Type", "application/json")
 * .header("Accept", "application/json") .header("Authorization", token)
 * .POST(HttpRequest.BodyPublishers.ofString(requestBody)) .build();
 * 
 * // Send the request and retrieve the response HttpResponse<String> response =
 * httpClient.send(request, HttpResponse.BodyHandlers.ofString());
 * 
 * // Print the response status code and body
 * System.out.println("Sell Order Response Code: " + response.statusCode());
 * System.out.println("Sell Order Response Body: " + response.body());
 * 
 * if (response.statusCode() == 200) { // Order successfully placed, update
 * status or take further action executedBuyOrders.remove(instrumentKey);
 * System.out.println("Sell Order placed successfully"); }
 * 
 * } catch (Exception e) { e.printStackTrace(); // Handle exceptions } }
 * 
 * 
 * }
 */

package com.example.demo.contoller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WatchlistEntity;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.serviceimpl.WatchlistServiceImpl;
import com.upstox.ApiClient;
import com.upstox.Configuration;
import com.upstox.auth.OAuth;
import com.upstox.feeder.MarketDataStreamer;
import com.upstox.feeder.MarketUpdate;
import com.upstox.feeder.MarketUpdate.MarketOHLC;
import com.upstox.feeder.constants.Mode;
import com.upstox.feeder.listener.OnMarketUpdateListener;

/*
 * @RestController public class PlaceOrderController {
 * 
 * private static final String ACCESS_TOKEN =
 * "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3NEFKRkwiLCJqdGkiOiI2NjI5YzkxZDM1MzM5ZTNiYjQwYTkwZDgiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsInNjb3BlIjpbImludGVyYWN0aXZlIiwiaGlzdG9yaWNhbCJdLCJpYXQiOjE3MTQwMTQ0OTMsImlzcyI6InVkYXBpLWdhdGV3YXktc2VydmljZSIsImV4cCI6MTcxNDA4MjQwMH0.JDfXykvWPkSkpGHfMY7krv8E7o-3jsy4DtmCHJY_TVg";
 * private Set<String> executedBuyOrders = new HashSet<>(); private Map<String,
 * Double> initialLTPMap = new HashMap<>(); private Map<String, Double>
 * buyPriceMap = new HashMap<>();
 * 
 * @Autowired private WatchlistRepository watchlistRepository;
 * 
 * @GetMapping("/getrealtimedata") public void getRealTimeUpdate() {
 * List<WatchlistEntity> watchlist = watchlistRepository.findAll(); Set<String>
 * instrumentKeys = new HashSet<>(); for (WatchlistEntity watchlistItem :
 * watchlist) { instrumentKeys.add(watchlistItem.getInstrumentKey()); }
 * ApiClient defaultClient = Configuration.getDefaultApiClient(); OAuth oAuth =
 * (OAuth) defaultClient.getAuthentication("OAUTH2");
 * oAuth.setAccessToken(ACCESS_TOKEN);
 * 
 * final MarketDataStreamer marketDataStreamer = new
 * MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);
 * 
 * marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
 * 
 * @Override public void onUpdate(MarketUpdate marketUpdate) { for
 * (Map.Entry<String, MarketUpdate.Feed> entry :
 * marketUpdate.getFeeds().entrySet()) { String instrumentKey = entry.getKey();
 * MarketUpdate.Feed marketUpdateFeed = entry.getValue();
 * 
 * if (marketUpdateFeed != null && marketUpdateFeed.getFf() != null &&
 * marketUpdateFeed.getFf().getMarketFF() != null) { MarketUpdate.LTPC ltpc =
 * marketUpdateFeed.getFf().getMarketFF().getLtpc(); if (ltpc != null) { Double
 * ltp = ltpc.getLtp(); if (!executedBuyOrders.contains(instrumentKey)) { double
 * buyPrice = ltp + 0.20; placeBuyOrderForInstrument(instrumentKey, buyPrice); }
 * else { sellOrderForInstrument(instrumentKey, ltp * 0.03); } } } } } });
 * 
 * marketDataStreamer.connect(); }
 * 
 * private void placeBuyOrderForInstrument(String instrumentKey, Double
 * buyPrice) { try { String url = "https://api.upstox.com/v2/order/place";
 * String token = "Bearer " + ACCESS_TOKEN; String requestBody =
 * "{\"quantity\": 1," + "\"product\": \"D\"," + "\"validity\": \"DAY\"," +
 * "\"price\": " + buyPrice + "," + "\"tag\": \"string\"," +
 * "\"instrument_token\": \"" + instrumentKey + "\"," +
 * "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," +
 * "\"disclosed_quantity\": 0," + "\"trigger_price\": " + buyPrice + "," +
 * "\"is_amo\": false" + "}";
 * 
 * HttpClient httpClient = HttpClient.newHttpClient(); HttpRequest request =
 * HttpRequest.newBuilder().uri(URI.create(url)) .header("Content-Type",
 * "application/json").header("Accept", "application/json")
 * .header("Authorization",
 * token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
 * 
 * HttpResponse<String> response = httpClient.send(request,
 * HttpResponse.BodyHandlers.ofString());
 * 
 * if (response.statusCode() == 200) { executedBuyOrders.add(instrumentKey);
 * System.out.println("Buy Order placed successfully"); } else {
 * System.out.println("Failed to place Buy Order: " + response.body()); } }
 * catch (Exception e) { e.printStackTrace(); } }
 * 
 * private void sellOrderForInstrument(String instrumentKey, Double
 * closingPrice) { try { String url = "https://api.upstox.com/v2/order/place";
 * String token = "Bearer " + ACCESS_TOKEN; String requestBody =
 * "{\"instrument_token\": \"" + instrumentKey + "\"," + "\"quantity\": 1," +
 * "\"product\": \"D\"," + "\"order_type\": \"LIMIT\"," +
 * "\"transaction_type\": \"SELL\"," + "\"price\": " + closingPrice + "," +
 * "\"trigger_price\": " + closingPrice + "," + "\"validity\": \"DAY\"," +
 * "\"disclosed_quantity\": 0," + "\"is_amo\": false" + "}";
 * 
 * HttpClient httpClient = HttpClient.newHttpClient(); HttpRequest request =
 * HttpRequest.newBuilder().uri(URI.create(url)) .header("Content-Type",
 * "application/json").header("Accept", "application/json")
 * .header("Authorization",
 * token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
 * 
 * HttpResponse<String> response = httpClient.send(request,
 * HttpResponse.BodyHandlers.ofString());
 * 
 * if (response.statusCode() == 200) { executedBuyOrders.remove(instrumentKey);
 * System.out.println("Sell Order placed successfully"); } } catch (Exception e)
 * { e.printStackTrace(); } } }
 */




@RestController
public class PlaceOrderControllerRealTime {

    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3UUJTOEYiLCJqdGkiOiI2NjNjZjIyNDk4ODdhODM4YmZjODJiZTAiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsInNjb3BlIjpbImludGVyYWN0aXZlIiwiaGlzdG9yaWNhbCJdLCJpYXQiOjE3MTUyNzAxODAsImlzcyI6InVkYXBpLWdhdGV3YXktc2VydmljZSIsImV4cCI6MTcxNTI5MjAwMH0.qcRKyXAsChW8lpKGXSPLDawcFOO8q1r-DPvxuPP6x9A";
    private Set<String> executedBuyOrders = new HashSet<>();
    private Map<String, Double> initialLTPMap = new HashMap<>();
    //private Map<String, Double> buyPriceMap = new HashMap<>();
    private Map<String, List<Double>> priceMap = new HashMap<>();
    
    @Autowired
    private WatchlistRepository watchlistRepository;

    @GetMapping("/getplaceorderrealtimedata")
    public void getPlaceOrderRealTimeUpdate() {
        List<WatchlistEntity> watchlist = watchlistRepository.findAll();
        Set<String> instrumentKeys = new HashSet<>();
        for (WatchlistEntity watchlistItem : watchlist) {
            instrumentKeys.add(watchlistItem.getInstrumentKey());
        }
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        OAuth oAuth = (OAuth) defaultClient.getAuthentication("OAUTH2");
        oAuth.setAccessToken(ACCESS_TOKEN);

        final MarketDataStreamer marketDataStreamer = new MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);

        marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
            @Override
            public void onUpdate(MarketUpdate marketUpdate) {
                for (Map.Entry<String, MarketUpdate.Feed> entry : marketUpdate.getFeeds().entrySet()) {
                    String instrumentKey = entry.getKey();
                    MarketUpdate.Feed marketUpdateFeed = entry.getValue();
                   
                    

                    if (marketUpdateFeed != null && marketUpdateFeed.getFf() != null && marketUpdateFeed.getFf().getMarketFF() != null) {
                        MarketUpdate.LTPC ltpc = marketUpdateFeed.getFf().getMarketFF().getLtpc();
                        MarketOHLC marketOHLC = marketUpdateFeed.getFf().getMarketFF().getMarketOHLC();
                        MarketUpdate.OHLC latestOHLC = marketOHLC.getOhlc().get(0);
                       
                        double openingPrice = latestOHLC.getOpen();
                        if (ltpc != null) {
                            Double ltp = ltpc.getLtp();
                          
                            if (!executedBuyOrders.contains(instrumentKey) && ltp>openingPrice) {
                                double buyPrice = ltp + ltp*0.01;                                
                                placeBuyOrderForInstrument(instrumentKey, buyPrice,ltp,openingPrice);
                            } else {
                            	if(executedBuyOrders.contains(instrumentKey)) {
                            	double sellPrice = 0.0;
                                sellOrderForInstrument(instrumentKey,sellPrice);
                            }
                            }
                        }
                        
                        double immediateSellForLtp=priceMap.get(instrumentKey).get(0);
                        double immediateSellForOpening=priceMap.get(instrumentKey).get(1);
                        if(immediateSellForLtp<immediateSellForOpening && executedBuyOrders.contains(instrumentKey)) {
                        		Double ltp = ltpc.getLtp();
                        		if(immediateSellForLtp*0.005<immediateSellForOpening) {
                        	 double immediateSellPrice = ltp - ltp*0.01;
                        	sellOrderForInstrument(instrumentKey,immediateSellPrice);
                        }
                        }
                    }
                }
            }
        });

        marketDataStreamer.connect();
    }

    private void placeBuyOrderForInstrument(String instrumentKey, Double buyPrice,Double ltp,double openingPrice) {
        try {
            String url = "https://api.upstox.com/v2/order/place";
            String token = "Bearer " + ACCESS_TOKEN;
            String requestBody = "{\"quantity\": 1," + "\"product\": \"D\"," + "\"validity\": \"DAY\"," +
                    "\"price\": " + buyPrice + "," + "\"tag\": \"string\"," +
                    "\"instrument_token\": \"" + instrumentKey + "\"," +
                    "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," +
                    "\"disclosed_quantity\": 0," + "\"trigger_price\": " + buyPrice + "," +
                    "\"is_amo\": false" + "}";

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    .header("Content-Type", "application/json").header("Accept", "application/json")
                    .header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                executedBuyOrders.add(instrumentKey);
                priceMap.put(instrumentKey, Arrays.asList(ltp, openingPrice));
                System.out.println("Buy Order placed successfully");
            } else {
                System.out.println("Failed to place Buy Order: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sellOrderForInstrument(String instrumentKey,Double sellPrice ) {
        try {
            Optional<WatchlistEntity> optionalEntity = watchlistRepository.findById(instrumentKey);
            if (optionalEntity.isPresent()) {
                WatchlistEntity watchlistEntity = optionalEntity.get();
                Double lastPrice = watchlistEntity.getLastPrice();
                if(sellPrice==0.0) {
                	  sellPrice=lastPrice + 0.03*lastPrice;
                }
               
                String url = "https://api.upstox.com/v2/order/place";
                String token = "Bearer " + ACCESS_TOKEN;
                String requestBody = "{\"instrument_token\": \"" + instrumentKey + "\"," + "\"quantity\": 1," +
                        "\"product\": \"D\"," + "\"order_type\": \"LIMIT\"," +
                        "\"transaction_type\": \"SELL\"," + "\"price\": " + sellPrice + "," +
                        "\"trigger_price\": " + sellPrice + "," + "\"validity\": \"DAY\"," +
                        "\"disclosed_quantity\": 0," + "\"is_amo\": false" + "}";

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                        .header("Content-Type", "application/json").header("Accept", "application/json")
                        .header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    executedBuyOrders.remove(instrumentKey);
                    priceMap.remove(instrumentKey);
                    System.out.println("Sell Order placed successfully");
                }
            } else {
                System.out.println("WatchlistEntity not found for instrumentKey: " + instrumentKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}