package com.comsense.assignment.service;

import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.repository.EmployeeReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
public class ReviewUpdateBufferServiceTest {

    @Mock
    private EmployeeReviewRepository mockRepo;

    @InjectMocks
    private ReviewUpdateBufferService service;

    @Test
    public void testProcessScheduledUpdates() {
        MockitoAnnotations.openMocks(this);

        // Prepare test data
        for (int i = 0; i < 50; i++) {
            EmployeeReview review = new EmployeeReview();
            // Set review properties
            service.updateReview(review);
        }

        // Call the scheduled method
        service.processScheduledUpdates();

        // Verify that saveAll was called once
        verify(mockRepo, times(5)).saveAll(anyList());
    }
}
