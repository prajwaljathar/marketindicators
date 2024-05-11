package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;

public class CommonMethodsUtils {
	
    @Autowired
    private static InstrumentRepository instrumentRepository;
	
    public static Map<String, String> readInstrumentDataFromDatabase() {
        Map<String, String> instrumentData = new HashMap<>();
        Iterable<Instrument> instruments = instrumentRepository.findAll();
        for (Instrument instrument : instruments) {
            instrumentData.put(instrument.getInstrumentKey(), instrument.getInstrumentName());
        }
        return instrumentData;
    }

}
