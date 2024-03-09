
package com.example.demo;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args){
		SpringApplication.run(DemoApplication.class, args);
		      
		
		/*
		 * ExcelReader excelReader = new ExcelReader(); String timestamp = new
		 * SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); String outputFilename
		 * = "D:\\Output_" + timestamp + ".xlsx";
		 * excelReader.readExcelAndCountOccurrences("D:\\Stock Pic.xlsx",
		 * outputFilename);	
		 */
		ExcelReader excelReader = new ExcelReader();
        String[] sheetNames = {"above 2%", "20% or above 18 %", "8% to 12%", "4% to 6%"};
        String filename = "D:\\Stock Pic.xlsx"; // Replace with your input file name
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String outputFilename = "D:\\Output_" + timestamp + ".xlsx";
        excelReader.readExcelAndCountOccurrences(filename, outputFilename, sheetNames);
        System.out.println("Included");
        System.out.println("Included");
       
		
		
	}
}
