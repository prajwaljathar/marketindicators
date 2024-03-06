
package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.net.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kong.unirest.Unirest;

@Controller
public class UpstoxApiInitializer {

	
	


	@GetMapping("/") public String displayHtml(@RequestParam(name = "code",
	  required = false) String authorizationCode) throws IOException,
	  InterruptedException {
	  
	  // Unirest.setTimeouts(0, 0); 
	 System.out.println("Access Token: " + authorizationCode);
	  
	  String redirectUrl = "redirect:" +
	  "https://api.upstox.com/v2/login/authorization/dialog" + "?client_id=" +
	  "39a83b5d-3396-4947-82cb-3e5f8465e171" + "&redirect_uri=" +
	  "https://5956-103-205-173-30.ngrok-free.app" + "&response_type=" + "code";
	  
	  return redirectUrl; }
	 

	
	  @GetMapping("/getaccesstoken") public String getAccessToken() {
	  
	  // Unirest.setTimeouts(0, 0); 
	  kong.unirest.HttpResponse<String> response =
	  Unirest.post("https://api.upstox.com/v2/login/authorization/token")
	  .header("Content-Type", "application/x-www-form-urlencoded")
	  .header("Accept", "application/json") .field("grant_type",
	  "authorization_code") .field("redirect_uri",
	  "https://5956-103-205-173-30.ngrok-free.app") .field("client_secret",
	  "aftixkz9rv") .field("client_id", "39a83b5d-3396-4947-82cb-3e5f8465e171")
	  .field("code", "6b54qi") .asString(); 
	  
	  System.out.println("Response Body " + response.getBody());
	  
	  return null; 
	  }
	 

	@GetMapping("/marketquotes")
	@ResponseBody
	public String marketQuotes() throws IOException, InterruptedException {
		String url = "https://api.upstox.com/v2/market-quote/quotes?instrument_key=NSE_EQ%7CINE848E01016,NSE_EQ|INE669E01016";
		String acceptHeader = "application/json";
		String authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3UUJTOEYiLCJqdGkiOiI2NWI5MjVmMzQ5N2U4OTQxYjg3OWE5MTQiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsInNjb3BlIjpbImludGVyYWN0aXZlIiwiaGlzdG9yaWNhbCJdLCJpYXQiOjE3MDY2MzI2OTEsImlzcyI6InVkYXBpLWdhdGV3YXktc2VydmljZSIsImV4cCI6MTcwNjY1MjAwMH0.bKNakghVkPag0q8h1p-fBmRDtHFUzaQjPPPtd4CTCdk";

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", acceptHeader)
				.header("Authorization", authorizationHeader).build();

		HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

		int statusCode = response.statusCode();
		HttpHeaders headers = response.headers();
		String responseBody = response.body();

		System.out.println("Status Code: " + statusCode);
		System.out.println("Response Headers: " + headers);
		System.out.println("Response Body: " + responseBody);
		return responseBody;
		

	}
}

