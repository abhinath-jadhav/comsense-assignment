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

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    @Mock
    private EmployeeReviewRepository employeeReviewRepository;

    @InjectMocks
    private UserReviewService userReviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetData() {
        EmployeeReview review = new EmployeeReview();
        review.setEmployeeId("E123");
        review.setReviewDate(ZonedDateTime.now());
        review.setGoal("Complete project");
        review.setAchievement("Achieved project goal");
        review.setRating(4.5f);
        review.setFeedback("Good performance");

        when(employeeReviewRepository.findAll()).thenReturn(List.of(review));

        Response response = userReviewService.getData();

        assert(response instanceof ReviewsResponse);
        assert(((ReviewsResponse) response).getReviews().size() == 1);
        assert(((ReviewsResponse) response).getReviews().get(0).getEmployeeId().equals("E123"));
    }
}
