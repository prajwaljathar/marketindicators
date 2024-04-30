package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "instrument_details")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instrumentKey;
    private String instrument_short_name;
    private String instument_name;
    private Double lastPrice;
    private Integer lotSize;
    private String instrumentType;
    private String exchange;
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
		return instrument_short_name;
	}
	public void setInstrumentShortName(String instrument_short_name) {
		this.instrument_short_name = instrument_short_name;
	}
	public String getInstrumentName() {
		return instument_name;
	}
	public void setInstrumentName(String instument_name) {
		this.instument_name = instument_name;
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

   
}
