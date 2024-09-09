package com.comsense.assignment.service;

import com.comsense.assignment.dto.Response;
import com.comsense.assignment.dto.ReviewsResponse;
import com.comsense.assignment.dto.SuccessResponse;
import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.repository.EmployeeReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReviewService {

    @Autowired
    private EmployeeReviewRepository employeeReviewRepository;

    @Autowired
    private ReviewUpdateBufferService bufferService;

    public Response getData() {

        List<EmployeeReview> reviews = employeeReviewRepository.findAll();
        return ReviewsResponse.builder()
                .status("200")
                .message("Successfully fetched review Data.")
                .reviews(reviews)
                .build();
    }

    public SuccessResponse update(EmployeeReview review) {
        bufferService.updateReview(review);
        return SuccessResponse.builder()
                .message("Request taken for update")
                .status("200")
                .build();
    }
}
