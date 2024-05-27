package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class WatchlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Instrument fields
    private String instrumentKey;
    private String instrumentShortName;
    private String instrumentName;
    private Double lastPrice;
    private Integer lotSize;
    private String instrumentType;
    private String exchange;
    

    // HistoricalData fields
    private String historicalInstrumentKey;
    private LocalDate historicalDate;
    private Double historicalPercentageChange;
    private String historicalInstrumentName;
    
    
    //Other
    private String trade;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInstrumentKey() {
		return instrumentKey;
	}
	public void setInstrumentKey(String instrumentKey) {
		this.instrumentKey = instrumentKey;
	}
	public String getInstrumentShortName() {
		return instrumentShortName;
	}
	public void setInstrumentShortName(String instrumentShortName) {
		this.instrumentShortName = instrumentShortName;
	}
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public Double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public Integer getLotSize() {
		return lotSize;
	}
	public void setLotSize(Integer lotSize) {
		this.lotSize = lotSize;
	}
	public String getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getHistoricalInstrumentKey() {
		return historicalInstrumentKey;
	}
	public void setHistoricalInstrumentKey(String historicalInstrumentKey) {
		this.historicalInstrumentKey = historicalInstrumentKey;
	}
	public LocalDate getHistoricalDate() {
		return historicalDate;
	}
	public void setHistoricalDate(LocalDate historicalDate) {
		this.historicalDate = historicalDate;
	}
	public Double getHistoricalPercentageChange() {
		return historicalPercentageChange;
	}
	public void setHistoricalPercentageChange(Double historicalPercentageChange) {
		this.historicalPercentageChange = historicalPercentageChange;
	}
	public String getHistoricalInstrumentName() {
		return historicalInstrumentName;
	}
	public void setHistoricalInstrumentName(String historicalInstrumentName) {
		this.historicalInstrumentName = historicalInstrumentName;
	}
	public String getTrade() {
		return trade;
	}
	public void setTrade(String trade) {
		this.trade = trade;
	}

    // Constructors, getters, and setters
}
