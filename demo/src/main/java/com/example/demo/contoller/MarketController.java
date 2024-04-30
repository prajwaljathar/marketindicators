package com.example.demo.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.MarketData;
import com.example.demo.repository.MarketDataRepository;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@RestController
public class MarketController {

    @Autowired
    private MarketDataRepository marketDataRepository;

    @GetMapping("/marketquotes")
    @ResponseBody
    public String marketQuotes() {
        String url = "https://api.upstox.com/v2/market-quote/quotes?instrument_key=NSE_EQ%7CINE669E01016";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data.containsKey("NSE_EQ:NHPC")) {
                Map<String, Object> marketQuote = (Map<String, Object>) data.get("NSE_EQ:NHPC");
                MarketData entity = new MarketData();
                entity.setStatus((String) marketQuote.get("status"));
                entity.setOhlcOpen(new BigDecimal((Double) marketQuote.get("ohlc_open")));
                entity.setOhlcHigh(new BigDecimal((Double) marketQuote.get("ohlc_high")));
                entity.setOhlcLow(new BigDecimal((Double) marketQuote.get("ohlc_low")));
                entity.setOhlcClose(new BigDecimal((Double) marketQuote.get("ohlc_close")));
                // Set other fields...
                entity.setDepthBuyQuantity((Integer) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_buy")).get(0)).get("quantity"));
                entity.setDepthBuyPrice(new BigDecimal((Double) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_buy")).get(0)).get("price")));
                entity.setDepthBuyOrders((Integer) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_buy")).get(0)).get("orders"));
                entity.setDepthSellQuantity((Integer) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_sell")).get(0)).get("quantity"));
                entity.setDepthSellPrice(new BigDecimal((Double) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_sell")).get(0)).get("price")));
                entity.setDepthSellOrders((Integer) ((Map<String, Object>) ((List<?>) marketQuote.get("depth_sell")).get(0)).get("orders"));
                entity.setTimestamp((String) marketQuote.get("timestamp"));
                entity.setInstrumentToken((String) marketQuote.get("instrument_token"));
                entity.setSymbol((String) marketQuote.get("symbol"));
                entity.setLastPrice(new BigDecimal((Double) marketQuote.get("last_price")));
                entity.setVolume((Integer) marketQuote.get("volume"));
                entity.setAveragePrice(new BigDecimal((Double) marketQuote.get("average_price")));
                entity.setOi((Integer) marketQuote.get("oi"));
                entity.setNetChange(new BigDecimal((Double) marketQuote.get("net_change")));
                entity.setTotalBuyQuantity((Integer) marketQuote.get("total_buy_quantity"));
                entity.setTotalSellQuantity((Integer) marketQuote.get("total_sell_quantity"));
                entity.setLowerCircuitLimit(new BigDecimal((Double) marketQuote.get("lower_circuit_limit")));
                entity.setUpperCircuitLimit(new BigDecimal((Double) marketQuote.get("upper_circuit_limit")));
                entity.setLastTradeTime((String) marketQuote.get("last_trade_time"));
                entity.setOiDayHigh(new BigDecimal((Double) marketQuote.get("oi_day_high")));
                entity.setOiDayLow(new BigDecimal((Double) marketQuote.get("oi_day_low")));

                // Save to database
                marketDataRepository.save(entity);
            }
        }

        return "Market quotes saved successfully";
    }
}

