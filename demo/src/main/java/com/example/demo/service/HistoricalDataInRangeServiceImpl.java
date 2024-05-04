package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.HistoricalDataInRangeEntity;
import com.example.demo.repository.HistoricalDataInRangeRepository;

@Service
public class HistoricalDataInRangeServiceImpl {

	   @Autowired
	    private HistoricalDataInRangeRepository historicalDataInRangeRepository;

	    public void saveDataToDatabase(Map<String, Map<String, Double>> dateInstrumentData) {
	        for (Map.Entry<String, Map<String, Double>> entry : dateInstrumentData.entrySet()) {
	            String instrumentKey = entry.getKey();
	            Map<String, Double> instrumentData = entry.getValue();
	            for (Map.Entry<String, Double> dataEntry : instrumentData.entrySet()) {
	                String date = dataEntry.getKey();
	                Double price = dataEntry.getValue();
	                HistoricalDataInRangeEntity entity = new HistoricalDataInRangeEntity(instrumentKey, date, price);
	                historicalDataInRangeRepository.save(entity);
	            }
	        }
	    }
}