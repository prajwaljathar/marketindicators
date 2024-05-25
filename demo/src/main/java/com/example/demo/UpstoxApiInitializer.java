
package com.example.demo;

/*import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.InstrumentKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.Unirest;
import com.mashape.unirest.http.JsonNode;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;



import java.net.URISyntaxException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocketImpl;

import org.java_websocket.WebSocketFactory;*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.Configuration;
import com.upstox.api.WebsocketAuthRedirectResponse;
import com.upstox.auth.OAuth;
import com.upstox.feeder.MarketDataStreamer;
import com.upstox.feeder.MarketUpdate;
import com.upstox.feeder.constants.Mode;
import com.upstox.feeder.listener.OnMarketUpdateListener;
import com.upstox.marketdatafeeder.rpc.proto.MarketDataFeed.Feed;
import com.upstox.marketdatafeeder.rpc.proto.MarketDataFeed.LTPC;

import io.swagger.client.api.WebsocketApi;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import com.google.protobuf.util.JsonFormat;

import okhttp3.*;

import com.example.demo.InstrumentKey; // Assuming this is a custom class in your project

import kong.unirest.Unirest;

@Controller
public class UpstoxApiInitializer {
	String code=null;

	@GetMapping("/")
	public String displayHtml(@RequestParam(name = "code", required = false) String authorizationCode)
			throws IOException, InterruptedException {
		

		// Unirest.setTimeouts(0, 0);
		System.out.println("code: " + authorizationCode);
		 code=authorizationCode;

		String redirectUrl = "redirect:" + "https://api.upstox.com/v2/login/authorization/dialog" + "?client_id="
				+ "39a83b5d-3396-4947-82cb-3e5f8465e171" + "&redirect_uri="
				+ "https://f72a-103-205-173-26.ngrok-free.app" + "&response_type=" + "code";

		return redirectUrl;
	}

	@GetMapping("/getaccesstoken")
	public String getAccessToken() {
		// Unirest.setTimeouts(0, 0);
		kong.unirest.HttpResponse<String> response = Unirest.post("https://api.upstox.com/v2/login/authorization/token")
				.header("Content-Type", "application/x-www-form-urlencoded").header("Accept", "application/json")
				.field("grant_type", "authorization_code")
				.field("redirect_uri", "https://f72a-103-205-173-26.ngrok-free.app")
				.field("client_secret", "aftixkz9rv").field("client_id", "39a83b5d-3396-4947-82cb-3e5f8465e171")
				.field("code", "Lt6CNh").asString();

		System.out.println("Response Body " + response.getBody());

		return response.getBody();
	}

	@GetMapping("/marketquotes")
	@ResponseBody
	public String marketQuotes() throws IOException, InterruptedException {
		String url = "https://api.upstox.com/v2/market-quote/quotes?instrument_key=NSE_EQ%7CINE669E01016";
		String acceptHeader = "application/json";
		String authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3UUJTOEYiLCJqdGkiOiI2NjQwOTJhMmYxYWFjNzBlMDBlNTVlODciLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzE1NTA3ODc0LCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MTU1NTEyMDB9.mKyCAlnmQt4CksmFKGxn7-tQnAb0B2RYC2phWr16nYc";

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
	public String historicalData(@RequestParam String fromDate, @RequestParam String toDate)
			throws UnsupportedEncodingException, EncryptedDocumentException {
		String excelFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";

		Map<String, String> instrumentData = readInstrumentDataFromExcel(excelFile);

		String[] instrumentKeys = instrumentData.keySet().toArray(new String[0]);

		StringBuilder responseBuilder = new StringBuilder();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(fromDate);
		LocalDate endDate = LocalDate.parse(toDate);

		responseBuilder.append("<table border=\"1\">");

		responseBuilder.append("<tr><th>Date</th>");
		for (String instrumentKey : instrumentKeys) {
			responseBuilder.append("<th>").append(instrumentData.get(instrumentKey)).append("</th>");
		}
		responseBuilder.append("</tr>");

		Map<String, Map<String, String>> dateInstrumentData = new LinkedHashMap<>();

		for (String instrumentKey : instrumentKeys) {
			try {
				String formattedStartDate = startDate.format(formatter);
				String formattedEndDate = endDate.format(formatter);
				System.out.println(instrumentKey);

				String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

				kong.unirest.HttpResponse<String> response = Unirest
						.get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/"
								+ formattedEndDate + "/" + formattedStartDate)
						.header("Accept", "application/json").asString();

				int statusCode = response.getStatus();
				String responseBody = response.getBody();

				if (statusCode == 200) {
					JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
					JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

					double previousClose = 0.0;

					for (int i = candleData.size() - 1; i >= 0; i--) {
						JsonArray candle = candleData.get(i).getAsJsonArray();
						String date = candle.get(0).getAsString().split("T")[0];
						double closePrice = candle.get(4).getAsDouble();

						if (previousClose != 0.0) {
							double percentageChange = ((closePrice - previousClose) / previousClose) * 100;
							String formattedPercentageChange = String.format("%.2f%%", percentageChange);

							dateInstrumentData.computeIfAbsent(date, k -> new HashMap<>()).put(instrumentKey,
									formattedPercentageChange != null ? formattedPercentageChange : "N/A");
						}

						previousClose = closePrice;
					}
				} else {
					responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
							.append("</td><td></td><td>Failed: ").append(statusCode).append("</td></tr>");
				}
			} catch (Exception e) {
				responseBuilder.append("<tr><td></td><td>").append(instrumentData.get(instrumentKey))
						.append("</td><td></td><td>Error: ").append(e.getMessage()).append("</td></tr>");
			}
		}

		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			String date = currentDate.format(formatter);

			responseBuilder.append("<tr><td>").append(date).append("</td>");

			for (String instrumentKey : instrumentKeys) {
				String percentageChange = "N/A";
				Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
				if (instrumentDataForDate != null) {
					percentageChange = instrumentDataForDate.getOrDefault(instrumentKey, "N/A");
				}

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

				responseBuilder.append("<td style=\"color:").append(color).append("\">").append(percentageChange)
						.append("</td>");
			}

			responseBuilder.append("</tr>");

			currentDate = currentDate.plusDays(1);
		}

		responseBuilder.append("</table>");
		writeDataToExcel(dateInstrumentData,
				"C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\historical_data.xls",
				instrumentData);
		System.out.println(responseBuilder);
		return responseBuilder.toString();
	}

	private Map<String, String> readInstrumentDataFromExcel(String excelFile) throws EncryptedDocumentException {
		Map<String, String> instrumentData = new HashMap<>();
		try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = WorkbookFactory.create(fis)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				Cell instrumentTypeCell = row.getCell(9);
				Cell instrumentKeyCell = row.getCell(0);
				Cell instrumentNameCell = row.getCell(3);

				if (instrumentTypeCell != null && instrumentTypeCell.getCellType() == CellType.STRING
						&& instrumentTypeCell.getStringCellValue().equals("EQUITY") && instrumentKeyCell != null
						&& instrumentNameCell != null) {
					String instrumentKey = instrumentKeyCell.getStringCellValue();
					String instrumentName = null;
					if (instrumentNameCell.getCellType() == CellType.STRING) {
						instrumentName = instrumentNameCell.getStringCellValue();
					} else if (instrumentNameCell.getCellType() == CellType.NUMERIC) {
						instrumentName = String.valueOf(instrumentNameCell.getNumericCellValue());
					}
					if (instrumentName != null) {
						instrumentData.put(instrumentKey, instrumentName);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instrumentData;
	}

	private void writeDataToExcel(Map<String, Map<String, String>> dateInstrumentData, String excelFilePath,
			Map<String, String> instrumentData) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Historical Data");

			// Create header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Date");
			int cellIndex = 1;
			for (String instrumentKey : instrumentData.keySet()) {
				if (cellIndex <= 16380) {
					headerRow.createCell(cellIndex++).setCellValue(instrumentData.get(instrumentKey));
				} else {
					break;
				}
			}

			// Cell Style for Positive Percentage Change (Red Background)
			CellStyle positiveStyle = workbook.createCellStyle();
			positiveStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			positiveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Populate data
			int rowIndex = 1;
			for (String date : dateInstrumentData.keySet()) {
				if (rowIndex > 1048576) {
					break;
				}
				Row row = sheet.createRow(rowIndex);
				row.createCell(0).setCellValue(date);
				cellIndex = 1;
				Map<String, String> instrumentDataForDate = dateInstrumentData.get(date);
				for (String instrumentKey : instrumentData.keySet()) {
					if (cellIndex <= 16380) {
						String percentageChange = instrumentDataForDate != null
								? instrumentDataForDate.get(instrumentKey)
								: "N/A";
						Cell cell = row.createCell(cellIndex++);
						cell.setCellValue(percentageChange);
						// Apply style based on percentage change
						if (!"N/A".equals(percentageChange) && percentageChange != null) {
							double percentageChangeValue = Double.parseDouble(percentageChange.replace("%", ""));
							if (percentageChangeValue >= 18
									|| (percentageChangeValue >= 8 && percentageChangeValue <= 12)
									|| (percentageChangeValue >= 4 && percentageChangeValue <= 6)) {
								cell.setCellStyle(positiveStyle);
							}
						}
					} else {
						break;
					}
				}
				rowIndex++;
			}

			// Write to file
			try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
				workbook.write(fos);
				System.out.println("Excel file written successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void placeOrderForInstrument(String instrumentKey) {

		try {
			String url = "https://api.upstox.com/v2/order/place";
			String token = "Bearer {your_access_token}";
			// Set up the request body
			String requestBody = "{" + "\"quantity\": 1," + "\"product\": \"D\"," + "\"validity\": \"DAY\","
					+ "\"price\": 13," + "\"tag\": \"string\"," + "\"instrument_token\": \"" + instrumentKey + "\","
					+ "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," + "\"disclosed_quantity\": 0,"
					+ "\"trigger_price\": 13.2," + "\"is_amo\": false" + "}";

			// Create the HttpClient
			HttpClient httpClient = HttpClient.newHttpClient();

			// Create the HttpRequest
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").header("Accept", "application/json")
					.header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

			// Send the request and retrieve the response
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Print the response status code and body
			System.out.println("Response Code: " + response.statusCode());
			System.out.println("Response Body: " + response.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/watchlist")
	public void createWatchlistExcel() {
		List<String> watchlist = new ArrayList<>();

		try {
			// Load Excel file
			FileInputStream file = new FileInputStream(
					"C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\historical_data.xls");
			Workbook workbook = WorkbookFactory.create(file);

			// Assuming the data is in the first sheet
			Sheet sheet = workbook.getSheetAt(0);

			// Find the last row and iterate over rows
			int lastRowNum = sheet.getLastRowNum();
			for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) { // Start from 1 to skip header row
				Row row = sheet.getRow(rowNum);
				if (row != null) {
					// Extract date and company data
					String date = row.getCell(0).getStringCellValue();
					for (int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++) {
						Cell cell = row.getCell(cellNum);
						if (cell != null) {
							String companyName = sheet.getRow(0).getCell(cellNum).getStringCellValue();

							// Retrieve percentage change
							String percentageChangeStr = "";
							if (cell.getCellType() == CellType.NUMERIC) {
								percentageChangeStr = String.valueOf(cell.getNumericCellValue());
							} else if (cell.getCellType() == CellType.STRING) {
								percentageChangeStr = cell.getStringCellValue();
							}

							// Check if percentageChangeStr is not empty
							if (!percentageChangeStr.isEmpty()) {
								// Remove percentage symbol and parse the value
								double percentageChange = Double.parseDouble(percentageChangeStr.replace("%", ""));

								// Check if the 3rd last record is not in the range of 4-6%
								if (rowNum == lastRowNum - 2) {
									if (percentageChange < 4 || percentageChange > 6) {
										// Check if the 2nd last and last records are in the range of 4-6%
										Row lastRow = sheet.getRow(lastRowNum);
										Cell lastCell = lastRow.getCell(cellNum);
										Cell secondLastCell = sheet.getRow(lastRowNum - 1).getCell(cellNum);
										if (lastCell != null && secondLastCell != null) {
											String lastPercentageChangeStr = lastCell.getStringCellValue();
											String secondLastPercentageChangeStr = secondLastCell.getStringCellValue();
											if (!lastPercentageChangeStr.isEmpty()
													&& !secondLastPercentageChangeStr.isEmpty()) {
												double lastPercentageChange = Double
														.parseDouble(lastPercentageChangeStr.replace("%", ""));
												double secondLastPercentageChange = Double
														.parseDouble(secondLastPercentageChangeStr.replace("%", ""));
												if (lastPercentageChange >= 4 && lastPercentageChange <= 6
														&& secondLastPercentageChange >= 4
														&& secondLastPercentageChange <= 6) {
													watchlist.add(companyName);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			// Close the original workbook
			workbook.close();

			// Create a new workbook to write the watchlist
			Workbook newWorkbook = new XSSFWorkbook();
			Sheet newSheet = newWorkbook.createSheet("Watchlist");
			Row headerRow = newSheet.createRow(0);
			headerRow.createCell(0).setCellValue("Company Name");

			// Write companies to the new Excel sheet
			for (int i = 0; i < watchlist.size(); i++) {
				Row row = newSheet.createRow(i + 1);
				row.createCell(0).setCellValue(watchlist.get(i));
			}

			// Write the new Excel file
			FileOutputStream outputStream = new FileOutputStream(
					"C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\watchlist.xlsx");
			newWorkbook.write(outputStream);
			newWorkbook.close();
			outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/placeorder")
	public String placeOrdersContinuously() {
		try {
			String url = "https://api.upstox.com/v2/order/place";
			String token = "Bearer {your_access_token}";

			// Create the HttpClient
			HttpClient httpClient = HttpClient.newHttpClient();

			// Continuously place orders for each instrument key
			for (InstrumentKey key : InstrumentKey.values()) {
				// Set up the request body
				String requestBody = "{" + "\"quantity\": 1," + "\"product\": \"D\"," + "\"validity\": \"DAY\","
						+ "\"price\": 13," + "\"tag\": \"string\"," + "\"instrument_token\": \"" + key.name() + "\","
						+ "\"order_type\": \"LIMIT\"," + "\"transaction_type\": \"BUY\"," + "\"disclosed_quantity\": 0,"
						+ "\"trigger_price\": 13.2," + "\"is_amo\": false" + "}";

				// Create the HttpRequest
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
						.header("Content-Type", "application/json").header("Accept", "application/json")
						.header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

				// Send the request and retrieve the response
				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

				// Print the response status code and body
				System.out.println("Response Code: " + response.statusCode());
				System.out.println("Response Body: " + response.body());

				// Add some delay between requests (optional)
				// Thread.sleep(1000); // 1 second delay
			}
			return "Orders placed successfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to place orders";
		}
	}


	
	/*
	 * @GetMapping("/getrealtimedata") public void getRealTimeUpdate() throws
	 * ApiException { ApiClient defaultClient = Configuration.getDefaultApiClient();
	 * 
	 * Set<String> instrumentKeys = new HashSet<>(); for (InstrumentKey key :
	 * InstrumentKey.values()) { instrumentKeys.add(key.name()); }
	 * 
	 * OAuth oAuth = (OAuth) defaultClient.getAuthentication("OAUTH2");
	 * oAuth.setAccessToken(ACCESS_TOKEN);
	 * 
	 * final MarketDataStreamer marketDataStreamer = new
	 * MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);
	 * 
	 * marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
	 * 
	 * @Override public void onUpdate(MarketUpdate marketUpdate) {
	 * System.out.println(marketUpdate); } });
	 * 
	 * marketDataStreamer.connect(); }
	 */

	/*
	 * @GetMapping("/getrealtimedata")
	 * 
	 * @ResponseBody public String initializeUpstoxApi() throws
	 * InvalidFormatException { List<String> columnData =
	 * readFirstColumnFromExcel(filePath); if (columnData != null &&
	 * !columnData.isEmpty()) { instruments = columnData.stream().map(item ->
	 * "BSE_EQ|" + item).collect(Collectors.toList());
	 * instruments.addAll(columnData.stream().map(item -> "NSE_EQ|" +
	 * item).collect(Collectors.toList())); }
	 * 
	 * String url = getUrl(); // OkHttpClient client = new OkHttpClient();
	 * 
	 * // OkHttpClient client = createInsecureOkHttpClient();
	 * 
	 * 
	 * // Request request = new Request.Builder() // .url(url) //
	 * .addHeader("Authorization", "Bearer " + ACCESS_TOKEN) //
	 * .addHeader("Api-Version", "2.0") // .build();
	 * 
	 * try { // Response response = client.newCall(request).execute(); // if
	 * (response.isSuccessful()) { // String responseBody =
	 * Objects.requireNonNull(response.body()).string(); // JsonObject jsonResponse
	 * = new Gson().fromJson(responseBody, JsonObject.class); // url =
	 * jsonResponse.getAsJsonObject("data").get("authorized_redirect_uri").
	 * getAsString();
	 * 
	 * Request webSocketRequest = new Request.Builder().url(url).build();
	 * WebSocketListener webSocketListener = new WebSocketListener() { public void
	 * onOpen(WebSocket webSocket, Response response) {
	 * sendSubscriptionMessage(webSocket); }
	 * 
	 * public void onFailure(IOException e, Response response) {
	 * e.printStackTrace(); }
	 * 
	 * public void onMessage(WebSocket webSocket, String text) {
	 * System.out.println("Received message: " + text); }
	 * 
	 * public void onClosing(WebSocket webSocket, int code, String reason) {
	 * System.out.println("WebSocket closing. Code: " + code + ", Reason: " +
	 * reason); }
	 * 
	 * // Implement other methods as needed };
	 * 
	 * WebSocket webSocket = client.newWebSocket(webSocketRequest,
	 * webSocketListener); // } } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * return "Upstox API initialized"; }
	 * 
	 * private String getUrl() { String url = ""; OkHttpClient client = new
	 * OkHttpClient();
	 * 
	 * 
	 * Request request = new Request.Builder()
	 * .url("https://api.upstox.com/v2/feed/market-data-feed/authorize")
	 * .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
	 * .addHeader("Api-Version", "2.0") .build();
	 * 
	 * try { Response response = client.newCall(request).execute(); if
	 * (response.isSuccessful()) { String responseBody =
	 * Objects.requireNonNull(response.body()).string(); JsonObject jsonResponse =
	 * new Gson().fromJson(responseBody, JsonObject.class); url =
	 * jsonResponse.getAsJsonObject("data").get("authorized_redirect_uri").
	 * getAsString(); } } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * return url; }
	 * 
	 * private List<String> readFirstColumnFromExcel(String filePath) throws
	 * InvalidFormatException { List<String> columnData = new ArrayList<>();
	 * 
	 * 
	 * try { File file = new File(filePath); if (file.exists()) { List<String> lines
	 * = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
	 * columnData.addAll(lines.stream().map(line ->
	 * line.split(",")[0]).collect(Collectors.toList())); } else {
	 * System.out.println("The specified Excel file does not exist."); } } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * 
	 * try (Workbook workbook = new XSSFWorkbook(new File(filePath))) { Sheet sheet
	 * = workbook.getSheetAt(0); // Assuming data is in the first sheet for (Row row
	 * : sheet) { Cell cell = row.getCell(0); // Assuming data is in the first
	 * column if (cell != null) { columnData.add(cell.getStringCellValue()); } } }
	 * catch (IOException e) { e.printStackTrace(); }
	 * 
	 * return columnData; }
	 * 
	 * private void sendSubscriptionMessage(WebSocket webSocket) { JsonObject
	 * message = new JsonObject(); message.addProperty("guid", "someguid");
	 * message.addProperty("method", "sub");
	 * 
	 * JsonObject data = new JsonObject(); data.addProperty("mode", "full");
	 * data.add("instrumentKeys", new Gson().toJsonTree(instruments));
	 * 
	 * message.add("data", data);
	 * 
	 * webSocket.send(message.toString()); }
	 * 
	 * 
	 */
	
	/*
	 * @GetMapping("/getrealtimedata") public String getRealTimeData() { try { //
	 * Define your access token String accessToken = "ACCESS_TOKEN";
	 * 
	 * // Get an authenticated API client ApiClient authenticatedClient =
	 * authenticateApiClient(accessToken);
	 * 
	 * // Get authorized WebSocket URI for market data feed URI serverUri =
	 * getAuthorizedWebSocketUri(authenticatedClient);
	 * 
	 * // Create and connect the WebSocket client WebSocketClient client =
	 * createWebSocketClient(serverUri); client.connect();
	 * 
	 * return "Initiated real-time data retrieval"; } catch (Exception e) {
	 * e.printStackTrace(); return "Error initiating real-time data retrieval"; } }
	 * 
	 * private ApiClient authenticateApiClient(String accessToken) { ApiClient
	 * defaultClient = Configuration.getDefaultApiClient(); OAuth oAuth = (OAuth)
	 * defaultClient.getAuthentication("OAUTH2"); oAuth.setAccessToken(accessToken);
	 * 
	 * return defaultClient; }
	 * 
	 * private URI getAuthorizedWebSocketUri(ApiClient authenticatedClient) throws
	 * ApiException { WebsocketApi websocketApi = new
	 * WebsocketApi(authenticatedClient); WebsocketAuthRedirectResponse response =
	 * websocketApi.getMarketDataFeedAuthorize("2.0");
	 * 
	 * return URI.create(response.getData().getAuthorizedRedirectUri()); }
	 * 
	 * private WebSocketClient createWebSocketClient(URI serverUri) { return new
	 * WebSocketClient(serverUri) {
	 * 
	 * @Override public void onOpen(ServerHandshake handshakedata) {
	 * System.out.println("Opened connection");
	 * 
	 * // Send subscription request after opening the connection
	 * sendSubscriptionRequest(this); }
	 * 
	 * @Override public void onMessage(String message) {
	 * System.out.println("Received: " + message); }
	 * 
	 * @Override public void onMessage(ByteBuffer bytes) {
	 * handleBinaryMessage(bytes); }
	 * 
	 * @Override public void onClose(int code, String reason, boolean remote) {
	 * System.out.println("Connection closed by " + (remote ? "remote peer" : "us")
	 * + ". Info: " + reason); }
	 * 
	 * @Override public void onError(Exception ex) { ex.printStackTrace(); } }; }
	 * 
	 * private void sendSubscriptionRequest(WebSocketClient client) { JsonObject
	 * requestObject = constructSubscriptionRequest(); byte[] binaryData =
	 * requestObject.toString().getBytes(StandardCharsets.UTF_8);
	 * 
	 * System.out.println("Sending: " + requestObject); client.send(binaryData); }
	 * 
	 * private JsonObject constructSubscriptionRequest() { JsonObject dataObject =
	 * new JsonObject(); dataObject.addProperty("mode", "full");
	 * 
	 * JsonArray instrumentKeys = new
	 * Gson().toJsonTree(Arrays.asList("NSE_INDEX|Nifty Bank",
	 * "NSE_INDEX|Nifty 50")) .getAsJsonArray(); dataObject.add("instrumentKeys",
	 * instrumentKeys);
	 * 
	 * JsonObject mainObject = new JsonObject(); mainObject.addProperty("guid",
	 * "someguid"); mainObject.addProperty("method", "sub"); mainObject.add("data",
	 * dataObject);
	 * 
	 * return mainObject; }
	 * 
	 * private void handleBinaryMessage(ByteBuffer bytes) {
	 * System.out.println("Received: " + bytes);
	 * 
	 * try { FeedResponse feedResponse = FeedResponse.parseFrom(bytes.array());
	 * 
	 * // Convert the protobuf object to a JSON string String jsonFormat =
	 * JsonFormat.printer().print(feedResponse);
	 * 
	 * // Print the JSON string System.out.println(jsonFormat);
	 * 
	 * } catch (InvalidProtocolBufferException e) {
	 * System.out.println("Received unparseable message"); e.printStackTrace(); } }
	 */
    
	/*
	 * private static List<String> readFirstColumnFromExcel(String filePath) throws
	 * IOException, InvalidFormatException { List<String> columnData = new
	 * ArrayList<>(); File file = new File(filePath); if (!file.exists()) {
	 * System.out.println("The specified Excel file does not exist."); return
	 * columnData; } try (Workbook workbook = new XSSFWorkbook(file)) { Sheet sheet
	 * = workbook.getSheetAt(0); // Assuming data is in the first sheet for (Row row
	 * : sheet) { Cell cell = row.getCell(0); // Assuming data is in the first
	 * column if (cell != null) { columnData.add(cell.getStringCellValue()); } } }
	 * return columnData; }
	 */
	
	
	private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiI3NEFKRkwiLCJqdGkiOiI2NjI5YzkxZDM1MzM5ZTNiYjQwYTkwZDgiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsInNjb3BlIjpbImludGVyYWN0aXZlIiwiaGlzdG9yaWNhbCJdLCJpYXQiOjE3MTQwMTQ0OTMsImlzcyI6InVkYXBpLWdhdGV3YXktc2VydmljZSIsImV4cCI6MTcxNDA4MjQwMH0.JDfXykvWPkSkpGHfMY7krv8E7o-3jsy4DtmCHJY_TVg";
	//private static List<String> instruments = new ArrayList<>();
	//private static final String filePath = "C:\\Users\\admin\\source\\repos\\Trading\\Trading\\Instruments.xlsx";	
	private Set<String> executedBuyOrders = new HashSet<>(); // Set to store executed buy orders
	private Map<String, Double> initialLTPMap = new HashMap<>();
	private Map<String, Double> buyPriceMap = new HashMap<>();// Map to store initial LTP for each instrument

	@GetMapping("/getrealtimedata")
	public void getRealTimeUpdate() throws ApiException {
	    ApiClient defaultClient = Configuration.getDefaultApiClient();

	    Set<String> instrumentKeys = new HashSet<>();
	    for (InstrumentKey key : InstrumentKey.values()) {
	        String name = key.name();
	        int secondUnderscoreIndex = name.indexOf('_', name.indexOf('_') + 1);
	        if (secondUnderscoreIndex != -1) {
	            name = name.substring(0, secondUnderscoreIndex) + "|" + name.substring(secondUnderscoreIndex + 1);
	        }
	        instrumentKeys.add(name);	
	      //  instrumentKeys.add(key.name());
	    }

	    OAuth oAuth = (OAuth) defaultClient.getAuthentication("OAUTH2");
	    oAuth.setAccessToken(ACCESS_TOKEN);

	    final MarketDataStreamer marketDataStreamer = new MarketDataStreamer(defaultClient, instrumentKeys, Mode.FULL);

	    marketDataStreamer.setOnMarketUpdateListener(new OnMarketUpdateListener() {
	        @Override
	        public void onUpdate(MarketUpdate marketUpdate) {
	            
	            // Iterate over the feeds and extract LTP
	            for (Map.Entry<String, MarketUpdate.Feed> entry : marketUpdate.getFeeds().entrySet()) {
	                String instrumentKey = entry.getKey();

	                MarketUpdate.Feed marketUpdateFeed = entry.getValue();
	                if (marketUpdateFeed != null && marketUpdateFeed.getFf() != null && marketUpdateFeed.getFf().getMarketFF() != null) {
	                    MarketUpdate.LTPC ltpc = marketUpdateFeed.getFf().getMarketFF().getLtpc();
	                    if (ltpc != null) {
	                        Double ltp = ltpc.getLtp();
	                       // initialLTPMap.put(instrumentKey, ltp);
	                       // Double initialLTP = initialLTPMap.get(instrumentKey);
	                            // Place buy order with a multiplier
	                            double buyPrice = ltp + 0.20; // Example multiplier, change as needed  
	                            if(executedBuyOrders.isEmpty()) {
	                            	placeBuyOrderForInstrumentKey(instrumentKey, buyPrice);	 	                            	
	                            	
	                            }	  
	                            
	                            	if (executedBuyOrders.contains(instrumentKey)) {
	    	                            // Check if current LTP is 4% higher than closingPrice    	                          
	    	                       //     if (initialLTP != null) { 
	    	                            	
	    	                            	//double buyPricePlaced=buyPriceMap.get(instrumentKey);
	    	                            	
	    	                       //     	if (ltp > closingPice * 0.03 ) {
		    	                                // Trigger sell order
	    	                            		
		    	                          
	                            		 sellOrderForInstrumentKey(instrumentKey,buyPrice * 0.03);
		    	                   //         }    	                            	
									/*
									 * if (ltp > initialLTP * 1.04) { // Trigger sell order
									 * sellOrderForInstrumentKey(instrumentKey,closingPrice * 1.04); }
									 */
	    	                       //     }	    	                            
	    	                        }                                                                                 
	                    }
	                }
	            }
	        }
	    });

	    marketDataStreamer.connect();
	}

	private void placeBuyOrderForInstrumentKey(String instrumentKey, Double buyPrice) {
	    try {
	        String url = "https://api.upstox.com/v2/order/place";
	        String token = "Bearer "+ ACCESS_TOKEN;

	        // Set up the request body for placing buy order
	        String requestBody = "{" +
	                "\"quantity\": 1," +
	                "\"product\": \"D\"," +
	                "\"validity\": \"DAY\"," +
	                "\"price\": " + buyPrice + "," +
	                "\"tag\": \"string\"," +
	                "\"instrument_token\": \"" + instrumentKey + "\"," +
	                "\"order_type\": \"LIMIT\"," +
	                "\"transaction_type\": \"BUY\"," +
	                "\"disclosed_quantity\": 0," +
	                "\"trigger_price\": " + buyPrice + "," +
	                "\"is_amo\": false" +
	                "}";

	        // Create the HttpClient
	        HttpClient httpClient = HttpClient.newHttpClient();

	        // Create the HttpRequest
	        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
	                .header("Content-Type", "application/json").header("Accept", "application/json")
	                .header("Authorization", token).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

	        // Send the request and retrieve the response
	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

	        // Print the response status code and body
	        System.out.println("Buy Order Response Code: " + response.statusCode());
	        System.out.println("Buy Order Response Body: " + response.body());

	        // Check if the buy order was successful
	        if (response.statusCode() == 200) {
	            // Order successfully placed, update status or take further action
	            executedBuyOrders.add(instrumentKey); // Update the set to indicate successful buy order execution
	            //buyPriceMap.put(instrumentKey, buyPrice);
	            System.out.println("Buy Order placed successfully");
	        } else {
	            // Order placement failed, handle accordingly
	            System.out.println("Failed to place Buy Order: " + response.body());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions
	    }
	}

	private void sellOrderForInstrumentKey(String instrumentKey,Double closingprice) {
	    try {
	        String url = "https://api.upstox.com/v2/order/place";
	        String token = "Bearer "+ ACCESS_TOKEN;

	        // Calculate the sell price (e.g., 4% higher than initial LTP)
	        Double sellPrice = closingprice ; // Change multiplier as needed
	       // Double sellPrice = ltp * 1.04; // Change multiplier as needed

	        // Set up the request body for selling the order
	        String requestBody = "{" +
	                "\"instrument_token\": \"" + instrumentKey + "\"," +
	                "\"quantity\": 1," +
	                "\"product\": \"D\"," +
	                "\"order_type\": \"LIMIT\"," +
	                "\"transaction_type\": \"SELL\"," +
	                "\"price\": " + sellPrice + "," +
	                "\"trigger_price\": " + sellPrice  + "," +
	                "\"validity\": \"DAY\"," +
	                "\"disclosed_quantity\": 0," +
	                "\"is_amo\": false" +
	                "}";

	        // Create the HttpClient
	        HttpClient httpClient = HttpClient.newHttpClient();

	        // Create the HttpRequest
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(url))
	                .header("Content-Type", "application/json")
	                .header("Accept", "application/json")
	                .header("Authorization", token)
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                .build();

	        // Send the request and retrieve the response
	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

	        // Print the response status code and body
	        System.out.println("Sell Order Response Code: " + response.statusCode());
	        System.out.println("Sell Order Response Body: " + response.body());
	        
	        if (response.statusCode() == 200) {
	            // Order successfully placed, update status or take further action
	            // Update the set to indicate successful Sell order execution
	            executedBuyOrders.remove(instrumentKey); 
	           // buyPriceMap.remove(instrumentKey); 
	            System.out.println("Sell Order placed successfully");
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/copy")
	public void  copy() {
	
	String watchlistPath = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\watchlist.xlsx";
    String completePath = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";
    String outputPath = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\output.xlsx";

    try {
        // Load watchlist.xlsx
        FileInputStream watchlistInput = new FileInputStream(watchlistPath);
        Workbook watchlistWorkbook = WorkbookFactory.create(watchlistInput);
        Sheet watchlistSheet = watchlistWorkbook.getSheetAt(0);

        // Load complete.xls
        FileInputStream completeInput = new FileInputStream(completePath);
        Workbook completeWorkbook = WorkbookFactory.create(completeInput);
        Sheet completeSheet = completeWorkbook.getSheetAt(0);

        // Create output workbook
        Workbook outputWorkbook = new XSSFWorkbook();
        Sheet outputSheet = outputWorkbook.createSheet();

        // Compare data and write to output
        for (Row watchlistRow : watchlistSheet) {
            Cell watchlistCell = watchlistRow.getCell(0);
            String watchlistData = watchlistCell.getStringCellValue();

            for (Row completeRow : completeSheet) {
                Cell completeCell = completeRow.getCell(3);
                if (completeCell != null && completeCell.getCellType() == CellType.STRING) {
                    String completeData = completeCell.getStringCellValue();
                    if (completeData.equals(watchlistData)) {
                        // If match found, write to output
                        Row outputRow = outputSheet.createRow(outputSheet.getLastRowNum() + 1);
                        Cell outputCell = outputRow.createCell(0);
                        outputCell.setCellValue(completeRow.getCell(0).getStringCellValue());
                        break;
                    }
                }
            }
        }

        // Write output to file
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        outputWorkbook.write(outputStream);

        // Close all streams
        watchlistInput.close();
        completeInput.close();
        outputStream.close();
        outputWorkbook.close();

        System.out.println("Output file generated successfully.");
    } catch (Exception e) {
        e.printStackTrace();
    }
	
	}
	
	
	
	
	
	
	
	 @GetMapping("/historicaldata/excel")
	    public String generateHistoricalDataExcel(@RequestParam String fromDate, @RequestParam String toDate)
	            throws UnsupportedEncodingException, EncryptedDocumentException {
	        String excelFile = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";

	        Map<String, String> instrumentData = readInstrumentDataFromExcel(excelFile);

	        StringBuilder responseBuilder = new StringBuilder();

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate startDate = LocalDate.parse(fromDate);
	        LocalDate endDate = LocalDate.parse(toDate);

	        Map<String, Map<String, Double>> dateInstrumentData = new LinkedHashMap<>();

	        for (String instrumentKey : instrumentData.keySet()) {
	            try {
	                String formattedStartDate = startDate.format(formatter);
	                String formattedEndDate = endDate.format(formatter);
	                System.out.println(instrumentKey);

	                String encodedInstrumentKey = URLEncoder.encode(instrumentKey, "UTF-8");

	                kong.unirest.HttpResponse<String> response = Unirest
	                        .get("https://api.upstox.com/v2/historical-candle/" + encodedInstrumentKey + "/day/"
	                                + formattedEndDate + "/" + formattedStartDate)
	                        .header("Accept", "application/json").asString();

	                int statusCode = response.getStatus();
	                String responseBody = response.getBody();

	                if (statusCode == 200) {
	                    JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
	                    JsonArray candleData = responseObject.getAsJsonObject("data").getAsJsonArray("candles");

	                    Map<String, Double> instrumentPriceData = new HashMap<>();

	                    for (int i = candleData.size() - 1; i >= 0; i--) {
	                        JsonArray candle = candleData.get(i).getAsJsonArray();
	                        String date = candle.get(0).getAsString().split("T")[0];
	                        double closePrice = candle.get(4).getAsDouble();

	                        instrumentPriceData.put(date, closePrice);
	                    }

	                    dateInstrumentData.put(instrumentKey, instrumentPriceData);
	                } else {
	                    responseBuilder.append("<p>Failed to retrieve data for instrument: ").append(instrumentKey)
	                            .append("</p>");
	                }
	            } catch (Exception e) {
	                responseBuilder.append("<p>Error: ").append(e.getMessage()).append("</p>");
	            }
	        }

	        // Save data to Excel
	        try {
	            saveDataToExcel(dateInstrumentData);
	            responseBuilder.append("<p>Excel file generated successfully.</p>");
	        } catch (IOException e) {
	            responseBuilder.append("<p>Error generating Excel file: ").append(e.getMessage()).append("</p>");
	        }

	        return responseBuilder.toString();
	    }

	    private void saveDataToExcel(Map<String, Map<String, Double>> dateInstrumentData) throws IOException {
	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Historical Data");

	        // Create header row
	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(0).setCellValue("Date");

	        Set<String> instrumentKeys = dateInstrumentData.keySet();
	        int cellNum = 1;
	        for (String instrumentKey : instrumentKeys) {
	            headerRow.createCell(cellNum++).setCellValue(instrumentKey);
	        }

	        // Populate data
	        int rowNum = 1;
	        for (String date : dateInstrumentData.get(instrumentKeys.iterator().next()).keySet()) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(date); // Date

	            int colNum = 1;
	            for (String instrumentKey : instrumentKeys) {
	                Double price = dateInstrumentData.get(instrumentKey).get(date);
	                row.createCell(colNum++).setCellValue(price != null ? price : 0.0);
	            }
	        }

	        // Write to file
	        try (FileOutputStream outputStream = new FileOutputStream("historical_data.xlsx")) {
	            workbook.write(outputStream);
	        }

	        workbook.close();
	    }
	
	    
	    
	    
	    
	
}
