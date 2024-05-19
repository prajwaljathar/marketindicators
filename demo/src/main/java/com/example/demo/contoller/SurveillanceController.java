package com.example.demo.contoller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.SurveillanceData;
import com.example.demo.repository.SurveillanceDataRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.InputStream;


@RestController
public class SurveillanceController {

    private static final String FILE_PATH = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\";

    @Autowired
    private SurveillanceDataRepository surveillanceDataRepository;

    @GetMapping("/surveillance/upload")
    public ResponseEntity<String> uploadSurveillanceData(@RequestParam("type") String type) {
        try {
            // Construct the complete file path
            String filePath = FILE_PATH + type;

            // Read CSV data
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            // Skip header
            reader.readLine();

            // Process each line
            String line;
            while ((line = reader.readLine()) != null) {
            	String[] fields;
                System.out.println("line: " + line);
                if (type.equals("asm-latest.csv")) {
                	fields = line.split("\t");
                }else {
                	 fields = line.split(",");
                }
                              
                System.out.println("fields: ");
                for (String field : fields) {
                    System.out.println(field);
                }

                // Ensure fields array has enough elements
                if (fields.length >= 4) {
                    // Assuming SurveillanceData has appropriate constructor and setters
                    SurveillanceData data = new SurveillanceData();
                    data.setSymbol(fields[1].trim());
                    data.setCompanyName(fields[2].trim());
                    data.setIsin(fields[3].trim());
                    data.setStage(fields[4].trim());

                    // Save data to the database
                    surveillanceDataRepository.save(data);
                } else {
                    // Handle case where fields array doesn't have enough elements
                    System.err.println("Incomplete data: " + line);
                }
            }

            reader.close();

            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}