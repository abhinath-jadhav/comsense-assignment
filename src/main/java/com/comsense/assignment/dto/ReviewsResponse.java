package com.comsense.assignment.dto;

import com.comsense.assignment.models.EmployeeReview;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewsResponse extends Response{
    private String status;
    private String message;
    private List<EmployeeReview> reviews;
}
