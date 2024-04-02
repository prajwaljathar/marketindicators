package com.example.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    public void readExcelAndCountOccurrences(String filename, String outputFilename, String[] sheetNames) {
        try (FileInputStream file = new FileInputStream(filename);
             Workbook workbook = WorkbookFactory.create(file)) {

            // Iterate over each sheet name provided
            for (String sheetName : sheetNames) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet != null) {
                    Map<String, List<Integer>> nameOccurrences = new HashMap<>();

                    // Iterate over rows starting from the second row
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            Cell nameCell = row.getCell(0); // Assuming name is in the first column (index 0)
                            if (nameCell != null) {
                                String name = nameCell.getStringCellValue().trim(); // Trim to remove leading/trailing spaces
                                if (!name.isEmpty()) {
                                    List<Integer> occurrences = nameOccurrences.getOrDefault(name, new ArrayList<>());
                                    occurrences.add(i);
                                    nameOccurrences.put(name, occurrences);
                                }
                            }
                        }
                    }

                    // Print duplicates
                    for (Map.Entry<String, List<Integer>> entry : nameOccurrences.entrySet()) {
                        if (entry.getValue().size() > 1) {
                          //  System.out.println("Duplicate name: " + entry.getKey() + ", Occurrences: " + entry.getValue());
                        }
                    }

                    // Write processed data to another Excel file
                    writeProcessedDataToExcel(nameOccurrences, outputFilename, sheetName);
                } else {
                    System.out.println("Sheet '" + sheetName + "' not found.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeProcessedDataToExcel(Map<String, List<Integer>> data, String outputFilename, String sheetName) {
        Workbook workbook;
        try {
            FileInputStream file = new FileInputStream(outputFilename);
            workbook = WorkbookFactory.create(file);
        } catch (IOException e) {
            // If the file doesn't exist, create a new workbook
            workbook = new XSSFWorkbook();
        }

        Sheet sheet = workbook.createSheet(sheetName + "_Processed_Data_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        int rownum = 0;

        // Add headers
        Row headerRow = sheet.createRow(rownum++);
        Cell header1 = headerRow.createCell(0);
        header1.setCellValue("Name");
        Cell header2 = headerRow.createCell(1);
        header2.setCellValue("Occurrences");
        Cell header3 = headerRow.createCell(2);
        header3.setCellValue("Row Numbers");

        // Add data
        for (Map.Entry<String, List<Integer>> entry : data.entrySet()) {
            Row row = sheet.createRow(rownum++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(entry.getKey()); // Name
            Cell cell2 = row.createCell(1);
            cell2.setCellValue(entry.getValue().size()); // Occurrences count
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(formatRowNumbers(entry.getValue())); // Row numbers
        }

        try (FileOutputStream fileOut = new FileOutputStream(outputFilename)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatRowNumbers(List<Integer> occurrences) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < occurrences.size(); i++) {
            sb.append(occurrences.get(i));
            if (i < occurrences.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ExcelReader excelReader = new ExcelReader();
        String filename = "input.xlsx"; // Replace with your input file name
        String outputFilename = "output.xlsx"; // Replace with your output file name
        String[] sheetNames = {"above 2%", "20% or above 18 %", "8% to 12%", "4% to 6%"};
        excelReader.readExcelAndCountOccurrences(filename, outputFilename, sheetNames);
    }
}