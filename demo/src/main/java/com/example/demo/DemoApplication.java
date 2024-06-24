
package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;

import com.example.demo.serviceimpl.InstrumentServiceImpl;
import com.example.demo.service.ExcelDataService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import com.example.demo.watchlist.Watchlist;

/*
 * @SpringBootApplication public class DemoApplication {
 * 
 * @Autowired private InstrumentServiceImpl instrumentserviceimpl;
 * 
 * @Autowired private InstrumentRepository instrumentRepository;
 * 
 * 
 * 
 * public static void main(String[] args) throws EncryptedDocumentException,
 * IOException {
 * 
 * SpringApplication.run(DemoApplication.class, args); DemoApplication demoApp =
 * new DemoApplication(); demoApp.loadDataFromExcel();
 * 
 * ExcelReader excelReader = new ExcelReader(); String[] sheetNames = {
 * "above 2%", "20% or above 18 %", "8% to 12%", "4% to 6%" }; String filename =
 * "D:\\Stock Pic.xlsx"; // Replace with your input file name String timestamp =
 * new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); String
 * outputFilename = "D:\\Output_" + timestamp + ".xlsx";
 * excelReader.readExcelAndCountOccurrences(filename, outputFilename,
 * sheetNames);
 * 
 * 
 * 
 * // Create Hibernate SessionFactory Configuration configuration = new
 * Configuration() .configure() // Load configuration from hibernate.cfg.xml
 * .addAnnotatedClass(com.example.demo.watchlist.Watchlist.class); // Add entity
 * class SessionFactory sessionFactory = configuration.buildSessionFactory();
 * 
 * 
 * // Create some hardcoded data Watchlist watchlist1 = new
 * Watchlist("Instrument 1", "15%", "2024-04-03"); Watchlist watchlist2 = new
 * Watchlist("Instrument 2", "20.5%", "2024-04-03");
 * 
 * // Open Hibernate session and transaction Session session
 * =sessionFactory.openSession(); Transaction transaction =
 * session.beginTransaction();
 * 
 * // Save the entities session.save(watchlist1); session.save(watchlist2);
 * 
 * // Commit transaction transaction.commit();
 * 
 * // Close session and session factory session.close(); sessionFactory.close();
 * 
 * 
 * }
 * 
 * private void loadDataFromExcel() throws IOException { String excelFilePath =
 * "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";
 * Workbook workbook = WorkbookFactory.create(new File(excelFilePath)); Sheet
 * sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
 * 
 * for (Row row : sheet) { // Skip header row if (row.getRowNum() == 0) {
 * continue; } Instrument instrument = new Instrument();
 * instrument.setInstrumentKey(row.getCell(0).getStringCellValue());
 * instrument.setTradingSymbol(row.getCell(2).getStringCellValue());
 * instrument.setName(row.getCell(3).getStringCellValue());
 * instrument.setLotSize((int) row.getCell(8).getNumericCellValue());
 * instrument.setInstrumentType(row.getCell(9).getStringCellValue());
 * instrument.setExchange(row.getCell(11).getStringCellValue());
 * 
 * instrumentRepository.save(instrument); } }
 * 
 * 
 * @PostConstruct public void init() { // Call methods from
 * InstrumentServiceImpl here instrumentserviceimpl.someMethod(); // Replace
 * someMethod() with an actual method in InstrumentServiceImpl }
 * 
 * }
 */



@SpringBootApplication
@EnableScheduling
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private ExcelDataService excelDataService;

	/*
	 * @PostConstruct public void init() { excelDataService.loadDataFromExcel(); }
	 */
	@pit
	
}


