package com.example.jpa.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.*;
import java.util.*;
@CrossOrigin(origins = "*")

@RestController
public class FileController {

    private final String FOLDER_PATH = "D:/splitFiles/";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()));

        List<String> chunk = new ArrayList<>();

        String line;
        int count = 0;
        int fileCount = 1;

        while ((line = reader.readLine()) != null) {

            chunk.add(line);
            count++;

            if (count == 5000) {

                Path output = Paths.get(FOLDER_PATH + "output_" + fileCount + ".txt");

                Files.write(output, chunk);

                chunk.clear();
                count = 0;
                fileCount++;
            }
        }

        if (!chunk.isEmpty()) {

            Path output = Paths.get(FOLDER_PATH + "output_" + fileCount + ".txt");

            Files.write(output, chunk);
        }

        return "File split successfully";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws Exception {

        Path path = Paths.get(FOLDER_PATH + fileName);

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .body(resource);
    }
    
    
    @PostMapping("/uploadCsv")
    public String uploadCsvFiles(@RequestParam("files") MultipartFile[] files) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Merged Data");

        int rowNum = 0;
        boolean headerWritten = false;

        for(MultipartFile file : files){

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream()));

            String line;
            boolean isHeader = true;

            while((line = reader.readLine()) != null){

                if(isHeader && headerWritten){
                    isHeader = false;
                    continue;
                }

                Row row = sheet.createRow(rowNum++);

                String[] data = line.split(",");

                for(int i=0;i<data.length;i++){
                    Cell cell = row.createCell(i);
                    cell.setCellValue(data[i]);
                }

                isHeader = false;
            }

            headerWritten = true;
        }

        FileOutputStream fos = new FileOutputStream("D:/merged.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();

        return "CSV files merged successfully";
    }
    
    
    
    
    
    @PostMapping("/mergeExcel")
    public String mergeExcelFiles(@RequestParam("files") MultipartFile[] files) throws Exception {

        SXSSFWorkbook mergedWorkbook = new SXSSFWorkbook();
        Sheet mergedSheet = mergedWorkbook.createSheet("Merged Data");

        int rowNum = 0;
        boolean headerWritten = false;

        for (MultipartFile file : files) {

            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getRowNum() == 0 && headerWritten) {
                    continue;
                }

                Row newRow = mergedSheet.createRow(rowNum++);

                for (Cell cell : row) {

                    Cell newCell = newRow.createCell(cell.getColumnIndex());

                    switch (cell.getCellType()) {
                        case STRING:
                            newCell.setCellValue(cell.getStringCellValue());
                            break;

                        case NUMERIC:
                            newCell.setCellValue(cell.getNumericCellValue());
                            break;

                        case BOOLEAN:
                            newCell.setCellValue(cell.getBooleanCellValue());
                            break;

                        default:
                            newCell.setCellValue(cell.toString());
                    }
                }
            }

            headerWritten = true;
            workbook.close();
        }

        FileOutputStream fos = new FileOutputStream("D:/merged.xlsx");
        mergedWorkbook.write(fos);

        fos.close();
        mergedWorkbook.dispose(); // important for temp file cleanup

        return "Excel files merged successfully";
    }
    
    
    
    
    @PostMapping("/mergeExcels")
    public String mergeExcelFiles() throws Exception {

        String folderPath = "D:/excelfiles/";
        File folder = new File(folderPath);

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));

        Workbook mergedWorkbook = new XSSFWorkbook();
        Sheet mergedSheet = mergedWorkbook.createSheet("Merged Data");

        int rowNum = 0;
        boolean headerWritten = false;

        for (File file : files) {

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getRowNum() == 0 && headerWritten) {
                    continue; // skip duplicate headers
                }

                Row newRow = mergedSheet.createRow(rowNum++);

                for (Cell cell : row) {

                    Cell newCell = newRow.createCell(cell.getColumnIndex());

                    switch (cell.getCellType()) {
                        case STRING:
                            newCell.setCellValue(cell.getStringCellValue());
                            break;

                        case NUMERIC:
                            newCell.setCellValue(cell.getNumericCellValue());
                            break;

                        case BOOLEAN:
                            newCell.setCellValue(cell.getBooleanCellValue());
                            break;

                        default:
                            newCell.setCellValue(cell.toString());
                    }
                }
            }

            headerWritten = true;
            workbook.close();
        }

        FileOutputStream fos = new FileOutputStream("D:/excelfiles/merged.xlsx");
        mergedWorkbook.write(fos);

        fos.close();
        mergedWorkbook.close();

        return "Excel files merged successfully";
    }
    
    
    
    @PostMapping("/mergeCsv")
    public String mergeCsvFiles() throws Exception {

        String folderPath = "D:/csvfiles/";
        File folder = new File(folderPath);

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Merged Data");

        int rowNum = 0;
        boolean headerWritten = false;

        for (File file : files) {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader && headerWritten) {
                    isHeader = false;
                    continue;
                }

                Row row = sheet.createRow(rowNum++);
                String[] data = line.split(",");

                for (int i = 0; i < data.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(data[i]);
                }

                isHeader = false;
            }

            headerWritten = true;
            reader.close();
        }

        FileOutputStream fos = new FileOutputStream("D:/csvfiles/merged.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();

        return "CSV files merged successfully";
    }
    
    
}




