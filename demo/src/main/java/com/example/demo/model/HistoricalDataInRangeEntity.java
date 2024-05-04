package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HistoricalDataInRangeEntity {

    public HistoricalDataInRangeEntity(String instrumentKey, String date, Double price) {
		super();		
		this.instrumentKey = instrumentKey;
		this.date = date;
		this.price = price;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instrumentKey;

    private String date;

    private Double price;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

    // Constructors, getters, and setters
}
