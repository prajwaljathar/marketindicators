package com.example.demo.contoller;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.CommonMethodsUtils;
import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;

@RestController
public class HistoricalCandlePerMin {
	
	
	 Map<String, String> instrumentData = CommonMethodsUtils.readInstrumentDataFromDatabase();
	 
	@GetMapping("/historicaldatapermin")
	public void historicalCandlePerSec() {	
	  String url = "https://api.upstox.com/v2/historical-candle/NSE_EQ%7CINE848E01016/1minute/2023-11-13/2023-11-12";

	    HttpClient httpClient = HttpClient.newHttpClient();
	    HttpRequest httpRequest = HttpRequest.newBuilder()
	            .uri(URI.create(url))
	            .header("Accept", "application/json")
	            .build();

	    try {
	        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

	        // Check the response status
	        if (httpResponse.statusCode() == 200) {
	            // Do something with the response body (e.g., print it)
	            System.out.println(httpResponse.body());
	        } else {
	            // Print an error message if the request was not successful
	            System.err.println("Error: " + httpResponse.statusCode() + " - " + httpResponse.body());
	        }
	    } catch (Exception e) {
	        // Handle exceptions
	        e.printStackTrace();
	    }
	}

}
