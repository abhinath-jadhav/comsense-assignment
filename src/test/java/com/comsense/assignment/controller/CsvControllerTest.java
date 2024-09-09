package com.comsense.assignment.controller;

import com.comsense.assignment.dto.CsvErrorResponse;
import com.comsense.assignment.dto.Response;
import com.comsense.assignment.dto.SuccessResponse;
import com.comsense.assignment.service.CsvService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CsvControllerTest {


    @Mock
    private CsvService csvService;

    @InjectMocks
    private CsvController csvController;

    @Test
    void testUploadCSVFileSuccess() throws Exception {
        // Load CSV file from resources
        ClassPathResource resource = new ClassPathResource("test-data.csv");
        MultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "text/csv",
                resource.getInputStream()
        );

        when(csvService.processCsv(any(MultipartFile.class)))
                .thenReturn(SuccessResponse.builder()
                        .status("200")
                        .message("CSV processed.").build());
        ResponseEntity<Response> response = csvController.uploadCSVFile(file);
        SuccessResponse errorResponse = (SuccessResponse) response.getBody();
        assert errorResponse != null;

        assertEquals("200",errorResponse.getStatus());
        assertEquals("CSV processed.",errorResponse.getMessage());


    }

    @Test
    void testUploadCSVFileFailure() throws Exception {
        // Load CSV file from resources
        ClassPathResource resource = new ClassPathResource("test-data.csv");
        MultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "text/csv",
                resource.getInputStream()
        );

        when(csvService.processCsv(any(MultipartFile.class)))
                .thenReturn(CsvErrorResponse.builder()
                        .message("CSV partially processed with some data violations.")
                        .status("400")
                        .build());
        ResponseEntity<Response> response = csvController.uploadCSVFile(file);
        CsvErrorResponse errorResponse = (CsvErrorResponse) response.getBody();
        assert errorResponse != null;
        assertEquals("400",errorResponse.getStatus());
        assertEquals("CSV partially processed with some data violations.",errorResponse.getMessage());

    }
}
