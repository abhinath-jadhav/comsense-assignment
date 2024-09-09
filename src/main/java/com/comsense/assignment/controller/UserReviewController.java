package com.comsense.assignment.controller;

import com.comsense.assignment.dto.Response;
import com.comsense.assignment.dto.SuccessResponse;
import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.service.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/review")
public class UserReviewController {

    @Autowired
    private UserReviewService userReviewService;

    @GetMapping
    public ResponseEntity<Response> getReviewData() {

        Response data = userReviewService.getData();
        return ResponseEntity.ok(data);

    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateResponse(@Valid @RequestBody EmployeeReview review){
        return  ResponseEntity.ok(userReviewService.update(review));
    }
}
