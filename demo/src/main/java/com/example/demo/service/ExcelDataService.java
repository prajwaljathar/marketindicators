package com.example.demo.service;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Instrument;
import com.example.demo.repository.InstrumentRepository;

@Service
public class ExcelDataService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    public void loadDataFromExcel() {
        try {
            String excelFilePath = "C:\\Users\\admin\\git\\marketindicators\\demo\\src\\main\\resources\\complete.xls";
            Workbook workbook = WorkbookFactory.create(new File(excelFilePath));
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            // Delete existing records from the database table
            instrumentRepository.deleteAll();

            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }
                Instrument instrument = new Instrument();
                instrument.setInstrumentKey(getStringValue(row.getCell(0)));
                instrument.setInstrumentShortName(getStringValue(row.getCell(2)));
                instrument.setInstrumentName(getStringValue(row.getCell(3)));
                instrument.setLotSize(getNumericValue(row.getCell(8))); // Use getNumericCellValue() for numeric cells
                instrument.setInstrumentType(getStringValue(row.getCell(9)));
                instrument.setExchange(getStringValue(row.getCell(11)));

                // Save the instrument
                instrumentRepository.save(instrument);
            }
            System.out.println("Done With Saving");
            workbook.close();
        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }
    }

    // Helper method to retrieve string value from a cell
    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            // Handle numeric value
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            // Handle string value
            return cell.getStringCellValue();
        }
    }

    // Helper method to retrieve numeric value from a cell
    private int getNumericValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0; // Or any default value as per your requirement
        }
        return (int) cell.getNumericCellValue();
    }
}