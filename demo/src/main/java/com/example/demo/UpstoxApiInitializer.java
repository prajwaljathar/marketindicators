
package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.net.URLEncoder;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import kong.unirest.Unirest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;



@Controller
public class UpstoxApiInitializer {

	@GetMapping("/")
	public String displayHtml(@RequestParam(name = "code",
	  required = false) String authorizationCode) throws IOException,
	  InterruptedException {
	  
	  // Unirest.setTimeouts(0, 0); 
	 System.out.println("Access Token: " + authorizationCode);
	  
	  String redirectUrl = "redirect:" +
	  "https://api.upstox.com/v2/login/authorization/dialog" + "?client_id=" +
	  "39a83b5d-3396-4947-82cb-3e5f8465e171" + "&redirect_uri=" +
	  "https://3592-103-205-173-17.ngrok-free.app" + "&response_type=" + "code";
	  
	  return redirectUrl; }
	 

	
	  @GetMapping("/getaccesstoken") 
	  public String getAccessToken() {  
	  // Unirest.setTimeouts(0, 0); 
	  kong.unirest.HttpResponse<String> response =
	  Unirest.post("https://api.upstox.com/v2/login/authorization/token")
	  .header("Content-Type", "application/x-www-form-urlencoded")
	  .header("Accept", "application/json") .field("grant_type",
	  "authorization_code") .field("redirect_uri",
	  "https://3592-103-205-173-17.ngrok-free.app") .field("client_secret",
	  "aftixkz9rv") .field("client_id", "39a83b5d-3396-4947-82cb-3e5f8465e171")
	  .field("code", "QNN4ZI") .asString(); 
	  
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

	  @GetMapping("/historicaldata")
	  @ResponseBody
	  public String historicalData(@RequestParam String fromDate, @RequestParam String toDate) throws UnsupportedEncodingException, InvalidFormatException {
	      // Define path to CSV file
	      //String csvFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.csv";
	    //  String csvFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";
	      String excelFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";


	      // Read instrument data from CSV
	      Map<String, String> instrumentData = readInstrumentDataFromExcel(excelFile);

	      // Define instrument keys for the stocks you want to query
	      String[] instrumentKeys = instrumentData.keySet().toArray(new String[0]);

	      StringBuilder responseBuilder = new StringBuilder();

	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	      LocalDate startDate = LocalDate.parse(fromDate);
	      LocalDate endDate = LocalDate.parse(toDate);

	      // Start of HTML table
	      responseBuilder.append("<table border=\"1\">");

	      // Table headers
	      responseBuilder.append("<tr><th>Date</th>");
	      for (String instrumentKey : instrumentKeys) {
	          responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).append("</th>");
	      }
	      responseBuilder.append("</tr>");

	      // Map to hold instrument data for each date
	      Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();

	      for (String instrumentKey : instrumentKeys) {
	          try {
	              // Format dates for API call
	              String formattedStartDate = startDate.format(formatter);
	              String formattedEndDate = endDate.format(formatter);
	              System.out.println(instrumentKey);

	              // Encode instrument key for URL
	              String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

	              // Make API call to retrieve historical stock data
	              kong.unirest.HttpResponse<String> response = Unirest.get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/" + formattedEndDate + "/" + formattedStartDate)
	                      .header("Accept", "application/json")
	                      .asString();

	              // Handle API response
	              int statusCode = response.getStatus();
	              String responseBody = response.getBody();

	              if (statusCode == 200) {
	                  // Parse JSON response
	                  JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
	                  JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

	                  // Initialize variables for tracking previous close price
	                  double previousClose = 0.0;

	                  // Loop through each candlestick data
	                  for (int i = candleData.size() - 1; i >= 0; i--) {
	                      JsonArray candle = candleData.get(i).getAsJsonArray();
	                      String date = candle.get(0).getAsString().split("T")[0];
	                      double closePrice = candle.get(4).getAsDouble();

	                      // Calculate percentage change
	                      if (previousClose != 0.0) {
	                          double percentageChange = ((closePrice - previousClose) / previousClose) * 100;
	                          String formattedPercentageChange = String.format("%.2f%%", percentageChange);

	                          // Store instrument data for this date
	                          dateInstrumentData.computeIfAbsent(date, k -> new HashMap<>())
	                          .put(instrumentKey, formattedPercentageChange != null ? formattedPercentageChange : "N/A");
	                      }

	                      // Update previous close price for the next iteration
	                      previousClose = closePrice;
	                  }
	              } else {
	                  // Handle API call failure
	                  responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey)).append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>");
	              }
	          } catch (Exception e) {
	              // Handle any exceptions
	              responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey)).append("</td><td></td><td>Error: ").append(e.getMessage()).append("</td></tr>");
	          }
	      }

	      // Populate the response builder with data for each date
	      LocalDate currentDate = startDate;
	      while (!currentDate.isAfter(endDate)) {
	          String date = currentDate.format(formatter);

	          responseBuilder.append("<tr><td>").append(date).append("</td>");

	          Map<String, String> instrumentDataForDate = dateInstrumentData.getOrDefault(date, new HashMap<>());
	          for (String instrumentKey : instrumentKeys) {
	              String percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");

	              // Add color based on percentage change range
	              String color = "";
	              if (!percentageChange.equals("N/A")) {
	                  double percentageChangeValue = Double.parseDouble(percentageChange.replace("%", ""));
	                  if (percentageChangeValue >= 18) {
	                      color = "red";
	                  } else if (percentageChangeValue >= 8 && percentageChangeValue <= 12) {
	                      color = "red";
	                  } else if (percentageChangeValue >= 4 && percentageChangeValue <= 6) {
	                      color = "red";
	                  }
	              }

	              responseBuilder.append("<td style=\"color:").append(color).append("\">").append(percentageChange).append("</td>");
	          }

	          responseBuilder.append("</tr>");

	          currentDate = currentDate.plusDays(1); // Move to the next date
	      }

	      // End of HTML table
	      responseBuilder.append("</table>");
	      System.out.println(responseBuilder);
	      return responseBuilder.toString();
	  }

	  // Define a method to read instrument keys and names from the CSV file
	  private Map<String, String> readInstrumentDataFromExcel(String excelFile) throws InvalidFormatException {
	        Map<String, String> instrumentData = new HashMap<>();
	        try (FileInputStream fis = new FileInputStream(excelFile);
	             Workbook workbook = WorkbookFactory.create(fis)) {
	            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

	            for (Row row : sheet) {
	                Cell instrumentTypeCell = row.getCell(9); // Assuming instrument type is in the second column
	                Cell instrumentKeyCell = row.getCell(0); // Assuming instrument key is in the first column
	                Cell instrumentNameCell = row.getCell(3); // Assuming instrument name is in the third column

	                if (instrumentTypeCell != null && instrumentTypeCell.getCellType() == CellType.STRING &&
	                    instrumentTypeCell.getStringCellValue().equals("EQUITY") &&
	                    instrumentKeyCell != null && instrumentNameCell != null) {
	                    String instrumentKey = instrumentKeyCell.getStringCellValue();
	                    String instrumentName = null;
	                    if (instrumentNameCell.getCellType() == CellType.STRING) {
	                        instrumentName = instrumentNameCell.getStringCellValue();
	                    } else if (instrumentNameCell.getCellType() == CellType.NUMERIC) {
	                        instrumentName = String.valueOf(instrumentNameCell.getNumericCellValue());
	                    }

	                    instrumentData.put(instrumentKey, instrumentName);
	                }
	            }
	        } catch (IOException | EncryptedDocumentException e) {
	            e.printStackTrace();
	        }
	        return instrumentData;
	    }


}

