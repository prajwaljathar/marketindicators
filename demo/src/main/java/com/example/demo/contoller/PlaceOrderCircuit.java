package com.example.demo.contoller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WatchlistEntity;
import com.example.demo.repository.WatchlistRepository;
import com.upstox.ApiClient;
import com.upstox.Configuration;
import com.upstox.auth.OAuth;
import com.upstox.feeder.MarketDataStreamer;
import com.upstox.feeder.MarketUpdate;
import com.upstox.feeder.constants.Mode;
import com.upstox.feeder.listener.OnMarketUpdateListener;


@RestController
public class PlaceOrderCircuit {

	private static final Logger logger = LogManager.getLogger(PlaceOrderControllerRealTime.class);

	private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3NEFKRkwiLCJqdGkiOiI2NjU1NGMyM2RhN2VkYTA0ZjZjZGRlYTkiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzE2ODY2MDgzLCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MTY5MzM2MDB9.fuIrrGcUkC1SQfenXY6Yun4irmS8QazVmCQrAMsgP-U";
	private Set<String> executedBuyOrders = new HashSet<>();
	private Map<String, Double> initialLTPMap = new HashMap<>();
	private Map<String, Double> priceMap = new HashMap<>();
	private Map<String, Double> closingPrices = new HashMap<>();
	Double ystdayInstrClsPrice;
	double tickSize = 0.05;

	@Autowired
	private WatchlistRepository watchlistRepository;

	@GetMapping("/getplaceordercircuit")
	public void getPlaceOrderRealTimeUpdate() throws IOException, InterruptedException {
		logger.info("Starting the process to fetch place order real-time update");
		//List<WatchlistEntity> watchlist = watchlistRepository.findAll();
		List<WatchlistEntity> watchlist =watchlistRepository.findByTradeYes5OrYes20();
		logger.debug("Fetched Watchlist: {}", watchlist);
		Set<String> instrumentKeys = new HashSet<>();
		for (WatchlistEntity watchlistItem : watchlist) {
			String instrumentKey = watchlistItem.getInstrumentKey();
			instrumentKeys.add(instrumentKey);
			ystdayInstrClsPrice = watchlistItem.getLastPrice();
			logger.debug("Instrument: {}, Yesterday's Closing Price: {}", instrumentKey, ystdayInstrClsPrice);
			if (ystdayInstrClsPrice != null) {
				closingPrices.put(instrumentKey, ystdayInstrClsPrice);
				logger.debug("Added to closing prices map: {} -> {}", instrumentKey, ystdayInstrClsPrice);
			}
		}

		ApiClient defaultClient = Configuration.getDefaultApiClient();
		OAuth oAuth = (OAuth) defaultClient.getAuthentication("OAUTH2");
		oAuth.setAccessToken(ACCESS_TOKEN);
		logger.info("OAuth token set for API client");

		final MarketDataStreamer marketDataStreamer = new MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);

		marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
			@Override
			public void onUpdate(MarketUpdate marketUpdate) {
				for (Map.Entry<String, MarketUpdate.Feed> entry : marketUpdate.getFeeds().entrySet()) {
					String instrumentKey = entry.getKey();
					MarketUpdate.Feed marketUpdateFeed = entry.getValue();
					logger.debug("Received market update for instrument: {}", instrumentKey);

					if (marketUpdateFeed != null && marketUpdateFeed.getFf() != null
							&& marketUpdateFeed.getFf().getMarketFF() != null) {
						MarketUpdate.LTPC ltpc = marketUpdateFeed.getFf().getMarketFF().getLtpc();
						if (ltpc != null) {
							Double ltp = ltpc.getLtp();
							logger.debug("Latest Trading Price (LTP) for {}: {}", instrumentKey, ltp);
							if (!executedBuyOrders.contains(instrumentKey) && closingPrices.containsKey(instrumentKey)
									&& ltp > (closingPrices.get(instrumentKey)
											+ closingPrices.get(instrumentKey) * 0.01)
									&& executedBuyOrders.size() < 15) {

								double buyPrice = closingPrices.get(instrumentKey) + closingPrices.get(instrumentKey) * 0.04;
								double roundedBuyPrice = Math.round(buyPrice / tickSize) * tickSize;
								roundedBuyPrice = Math.round(roundedBuyPrice * 100.0) / 100.0;
								logger.info("Ready to place Buy Order for {}: Buy Price: {}", instrumentKey,
										roundedBuyPrice);
								placeBuyOrderForInstrument(instrumentKey, roundedBuyPrice);
							} 
							
							
//							else {
//								if (executedBuyOrders.contains(instrumentKey) && executedBuyOrders.size() < 5) {
//									double sellPrice = 0;
//									logger.info("Ready to place Sell Order for {}: Sell Price: {}", instrumentKey,
//											sellPrice);
//									sellOrderForInstrument(instrumentKey, sellPrice);
//								}
//							}
						}
						
//						if (!priceMap.isEmpty()) {
//							logger.debug("Price Map: {}", priceMap);
//							if (priceMap.containsKey(instrumentKey)) {
//								double buyPriceForImmediateSell = priceMap.get(instrumentKey);
//								Double ltp = ltpc.getLtp();
//								logger.debug("Immediate sell check for {}: Current LTP: {}, Buy Price: {}",
//										instrumentKey, ltp, buyPriceForImmediateSell);
//								if (ltp < buyPriceForImmediateSell && executedBuyOrders.contains(instrumentKey)
//										&& executedBuyOrders.size() < 5) {
//									if (ltp < buyPriceForImmediateSell * 0.95) {
//										double immediateSellPrice = ltp - ltp * 0.01;
//										double roundedSellPrice = Math.round(immediateSellPrice / tickSize) * tickSize;
//										roundedSellPrice = Math.round(roundedSellPrice * 100.0) / 100.0;
//										logger.info("Immediate Sell for {}: Immediate Sell Price: {}", instrumentKey,
//												roundedSellPrice);
//										sellOrderForInstrument(instrumentKey, roundedSellPrice);
//									}
//								}
//							}
//						}
						
					} else {
						logger.warn("Market update feed is null or incomplete for instrument: {}", instrumentKey);
					}
				}
			}
		});

		marketDataStreamer.connect();
		logger.info("Market data streamer connected");
	}

	private void placeBuyOrderForInstrument(String instrumentKey, Double buyPrice) {
		try {
			logger.debug("Preparing to place Buy Order for instrument: {}, Buy Price: {}", instrumentKey, buyPrice);
			String urlPlcOrd = "https://api.upstox.com/v2/order/place";
			String token = "Bearer " + ACCESS_TOKEN;
			String requestBody = "{\"quantity\": 4," + "\"product\": \"D\"," + "\"validity\": \"DAY\"," + "\"price\": "
					+ buyPrice + "," + "\"tag\": \"string\"," + "\"instrument_token\": \"" + instrumentKey + "\","
					+ "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," + "\"disclosed_quantity\": 0,"
					+ "\"trigger_price\": " + buyPrice + "," + "\"is_amo\": false" + "}";

			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlPlcOrd))
					.header("Content-Type", "application/json").header("Accept", "application/json")
					.header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JSONObject jsonResponse = new JSONObject(response.body());
				String status = jsonResponse.optString("status");
				logger.debug("Response from Buy Order API: {}", jsonResponse);

				if ("success".equals(status)) {
					executedBuyOrders.add(instrumentKey);
					priceMap.put(instrumentKey, buyPrice);
					logger.info("Buy Order placed successfully for {} at price {}", instrumentKey, buyPrice);
				} else {
					logger.error("Failed to place Buy Order for {}: {}", instrumentKey, response.body());
				}
			} else {
				logger.error("Failed to place Buy Order for {}: Status Code: {}", instrumentKey, response.statusCode());
			}
		} catch (Exception e) {
			logger.error("Exception while placing Buy Order for {}: ", instrumentKey, e);
		}
	}

//	private void sellOrderForInstrument(String instrumentKey, Double roundedSellPrice) {
//		try {
//			logger.debug("Preparing to place Sell Order for instrument: {}, Sell Price: {}", instrumentKey,
//					roundedSellPrice);
//			Optional<WatchlistEntity> optionalEntity = watchlistRepository.findByInstrumentKey(instrumentKey);
//			if (optionalEntity.isPresent()) {
//				WatchlistEntity watchlistEntity = optionalEntity.get();
//				Double lastPrice = watchlistEntity.getLastPrice();
//				logger.debug("Last Price for {}: {}", instrumentKey, lastPrice);
//				if (roundedSellPrice == 0) {
//					roundedSellPrice = lastPrice + 0.03 * lastPrice;
//					roundedSellPrice = Math.round(roundedSellPrice / tickSize) * tickSize;
//					roundedSellPrice = Math.round(roundedSellPrice * 100.0) / 100.0;
//					logger.debug("Calculated new Sell Price for {}: {}", instrumentKey, roundedSellPrice);
//				}
//
//				String url = "https://api.upstox.com/v2/order/place";
//				String token = "Bearer " + ACCESS_TOKEN;
//				String requestBody = "{\"instrument_token\": \"" + instrumentKey + "\"," + "\"quantity\": 4,"
//						+ "\"product\": \"D\"," + "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"SELL\","
//						+ "\"price\": " + roundedSellPrice + "," + "\"trigger_price\": " + roundedSellPrice + ","
//						+ "\"validity\": \"DAY\"," + "\"disclosed_quantity\": 0," + "\"is_amo\": false" + "}";
//
//				HttpClient httpClient = HttpClient.newHttpClient();
//				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
//						.header("Content-Type", "application/json").header("Accept", "application/json")
//						.header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
//
//				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//				if (response.statusCode() == 200) {
//					JSONObject jsonResponse = new JSONObject(response.body());
//					String status = jsonResponse.optString("status");
//					logger.debug("Response from Sell Order API: {}", jsonResponse);
//
//					if ("success".equals(status)) {
//						logger.info("Sell Order placed successfully for {}", instrumentKey);
//					} else {
//						logger.error("Sell Order placement failed for {}: {}", instrumentKey, response.body());
//					}
//				} else {
//					logger.error("Sell Order placement failed for {}: Status Code: {}", instrumentKey,
//							response.statusCode());
//				}
//			} else {
//				logger.warn("WatchlistEntity not found for instrumentKey: {}", instrumentKey);
//			}
//		} catch (Exception e) {
//			logger.error("Exception while placing Sell Order for {}: ", instrumentKey, e);
//		}
//	}
}