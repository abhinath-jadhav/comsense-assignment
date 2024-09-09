package com.comsense.assignment.controller;

import com.comsense.assignment.dto.Response;
import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @PostMapping("/csv")
    public ResponseEntity<Response> uploadCSVFile(@RequestParam("file") MultipartFile file) {

            return ResponseEntity.ok(csvService.processCsv(file));

    }
}
