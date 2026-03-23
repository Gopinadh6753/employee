package com.example.jpa.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.jpa.model.Employee;
import com.example.jpa.repository.EmployeeRepository;


@RestController
@RequestMapping("/excel")
@CrossOrigin(origins = "*")
public class EmployeeExcelController {

    @Autowired
    private EmployeeRepository repo;

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateExcel(@RequestParam("file") MultipartFile file) throws IOException {
        // Read input Excel
        List<String> employeeNames = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    employeeNames.add(cell.getStringCellValue());
                }
            }
        }

        // Create output workbook
        XSSFWorkbook outWorkbook = new XSSFWorkbook();
        XSSFSheet outSheet = outWorkbook.createSheet("Employee Details");

        // Header Row
        Row header = outSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Department");
        header.createCell(3).setCellValue("Email");
        header.createCell(4).setCellValue("Salary");

        int rowIdx = 1;
        for (String name : employeeNames) {
            Optional<Employee> emp = repo.findByNameIgnoreCase(name);
            if (emp.isPresent()) {
                Employee e = emp.get();
                Row row = outSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getName());
                row.createCell(2).setCellValue(e.getDepartment());
                row.createCell(3).setCellValue(e.getEmail());
                row.createCell(4).setCellValue(e.getSalary());
            }
        }

        // Write to ByteArray
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        outWorkbook.write(bos);
        outWorkbook.close();

        ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employee_Details.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}
