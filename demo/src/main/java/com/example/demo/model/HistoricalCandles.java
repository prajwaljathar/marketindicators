package com.example.demo.model;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Integer volume;
    private int openInterest;
    private long dateDifference; 
    

	public String getIntervalType() {
		return intervalType;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	private String instrumentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal percentageChange;
    
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

    public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getPercentageChange() {
		return percentageChange;
	}
	public void setPercentageChange(BigDecimal percentageChange) {
		this.percentageChange = percentageChange;
	}
	 public long getDateDifference() {
		return dateDifference;
	}
	public void setDateDifference(long dateDifference) {
		this.dateDifference = dateDifference;
	}
	
    // Constructors, getters, and setters
}