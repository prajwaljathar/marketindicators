package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "market_data")
public class MarketData {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private BigDecimal ohlcOpen;
    private BigDecimal ohlcHigh;
    private BigDecimal ohlcLow;
    private BigDecimal ohlcClose;
    private Integer depthBuyQuantity;
    private BigDecimal depthBuyPrice;
    private Integer depthBuyOrders;
    private Integer depthSellQuantity;
    private BigDecimal depthSellPrice;
    private Integer depthSellOrders;
    private String timestamp;
    private String instrumentToken;
    private String symbol;
    private BigDecimal lastPrice;
    private Integer volume;
    private BigDecimal averagePrice;
    private Integer oi;
    private BigDecimal netChange;
    private Integer totalBuyQuantity;
    private Integer totalSellQuantity;
    private BigDecimal lowerCircuitLimit;
    private BigDecimal upperCircuitLimit;
    private String lastTradeTime;
    private BigDecimal oiDayHigh;
    private BigDecimal oiDayLow;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getOhlcOpen() {
		return ohlcOpen;
	}
	public void setOhlcOpen(BigDecimal ohlcOpen) {
		this.ohlcOpen = ohlcOpen;
	}
	public BigDecimal getOhlcHigh() {
		return ohlcHigh;
	}
	public void setOhlcHigh(BigDecimal ohlcHigh) {
		this.ohlcHigh = ohlcHigh;
	}
	public BigDecimal getOhlcLow() {
		return ohlcLow;
	}
	public void setOhlcLow(BigDecimal ohlcLow) {
		this.ohlcLow = ohlcLow;
	}
	public BigDecimal getOhlcClose() {
		return ohlcClose;
	}
	public void setOhlcClose(BigDecimal ohlcClose) {
		this.ohlcClose = ohlcClose;
	}
	public Integer getDepthBuyQuantity() {
		return depthBuyQuantity;
	}
	public void setDepthBuyQuantity(Integer depthBuyQuantity) {
		this.depthBuyQuantity = depthBuyQuantity;
	}
	public BigDecimal getDepthBuyPrice() {
		return depthBuyPrice;
	}
	public void setDepthBuyPrice(BigDecimal depthBuyPrice) {
		this.depthBuyPrice = depthBuyPrice;
	}
	public Integer getDepthBuyOrders() {
		return depthBuyOrders;
	}
	public void setDepthBuyOrders(Integer depthBuyOrders) {
		this.depthBuyOrders = depthBuyOrders;
	}
	public Integer getDepthSellQuantity() {
		return depthSellQuantity;
	}
	public void setDepthSellQuantity(Integer depthSellQuantity) {
		this.depthSellQuantity = depthSellQuantity;
	}
	public BigDecimal getDepthSellPrice() {
		return depthSellPrice;
	}
	public void setDepthSellPrice(BigDecimal depthSellPrice) {
		this.depthSellPrice = depthSellPrice;
	}
	public Integer getDepthSellOrders() {
		return depthSellOrders;
	}
	public void setDepthSellOrders(Integer depthSellOrders) {
		this.depthSellOrders = depthSellOrders;
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
	public BigDecimal getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}
	public Integer getOi() {
		return oi;
	}
	public void setOi(Integer oi) {
		this.oi = oi;
	}
	public BigDecimal getNetChange() {
		return netChange;
	}
	public void setNetChange(BigDecimal netChange) {
		this.netChange = netChange;
	}
	public Integer getTotalBuyQuantity() {
		return totalBuyQuantity;
	}
	public void setTotalBuyQuantity(Integer totalBuyQuantity) {
		this.totalBuyQuantity = totalBuyQuantity;
	}
	public Integer getTotalSellQuantity() {
		return totalSellQuantity;
	}
	public void setTotalSellQuantity(Integer totalSellQuantity) {
		this.totalSellQuantity = totalSellQuantity;
	}
	public BigDecimal getLowerCircuitLimit() {
		return lowerCircuitLimit;
	}
	public void setLowerCircuitLimit(BigDecimal lowerCircuitLimit) {
		this.lowerCircuitLimit = lowerCircuitLimit;
	}
	public BigDecimal getUpperCircuitLimit() {
		return upperCircuitLimit;
	}
	public void setUpperCircuitLimit(BigDecimal upperCircuitLimit) {
		this.upperCircuitLimit = upperCircuitLimit;
	}
	public String getLastTradeTime() {
		return lastTradeTime;
	}
	public void setLastTradeTime(String lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}
	public BigDecimal getOiDayHigh() {
		return oiDayHigh;
	}
	public void setOiDayHigh(BigDecimal oiDayHigh) {
		this.oiDayHigh = oiDayHigh;
	}
	public BigDecimal getOiDayLow() {
		return oiDayLow;
	}
	public void setOiDayLow(BigDecimal oiDayLow) {
		this.oiDayLow = oiDayLow;
	}

   
}
