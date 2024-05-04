package com.example.demo.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "historical_candles")
public class HistoricalCandles {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String instrumentKey;
    private String intervalType;
    private LocalDateTime candleTime;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private int volume;
    private int openInterest;
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
	public String getInterval() {
		return intervalType;
	}
	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}
	public LocalDateTime getCandleTime() {
		return candleTime;
	}
	public void setCandleTime(LocalDateTime candleTime) {
		this.candleTime = candleTime;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getOpenInterest() {
		return openInterest;
	}
	public void setOpenInterest(int openInterest) {
		this.openInterest = openInterest;
	}

    // Constructors, getters, and setters
}