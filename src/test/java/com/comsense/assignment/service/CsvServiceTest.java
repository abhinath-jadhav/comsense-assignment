package com.comsense.assignment.service;

import com.comsense.assignment.dto.Response;
import com.comsense.assignment.dto.ReviewsResponse;
import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.repository.EmployeeReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import javax.validation.Validator;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CsvServiceTest {

    @InjectMocks
    private CsvService csvService;

    @Mock
    private EmployeeReviewRepository employeeReviewRepository;

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessCsv() throws Exception {

        ClassPathResource resource = new ClassPathResource("test-data.csv");
        MultipartFile file = new MockMultipartFile(
                resource.getFilename(),
                resource.getFilename(),
                "text/csv",
                resource.getInputStream()
        );

        when(validator.validate(any(EmployeeReview.class))).thenReturn(new HashSet<>());

        csvService.processCsv(file);
    }

}
