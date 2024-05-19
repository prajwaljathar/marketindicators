package com.example.demo.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RestController
public class SurveillanceController {

    private static final String FILE_PATH = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\";

    @Autowired
    private SurveillanceDataRepository surveillanceDataRepository;

    @PostMapping("/surveillance/upload")
    public ResponseEntity<String> uploadSurveillanceData(@RequestParam("type") String type) {
        try {
            // Construct the complete file path
            String filePath = FILE_PATH + type;

            // Read CSV data
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            // Skip header
            String header = reader.readLine();

            // Process each line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");

                // Assuming SurveillanceData has appropriate constructor and setters
                SurveillanceData data = new SurveillanceData();
                data.setSymbol(fields[1].trim());
                data.setCompanyName(fields[2].trim());
                data.setIsin(fields[3].trim());
                data.setStage(fields[4].trim());

                // Save data to the database
                surveillanceDataRepository.save(data);
            }

            reader.close();

            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}
