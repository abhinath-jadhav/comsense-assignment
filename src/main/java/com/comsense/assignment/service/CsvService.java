package com.comsense.assignment.service;

import com.comsense.assignment.dto.CsvErrorResponse;
import com.comsense.assignment.dto.Response;
import com.comsense.assignment.dto.ReviewsResponse;
import com.comsense.assignment.dto.SuccessResponse;
import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.repository.EmployeeReviewRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvService {

    @Autowired
    private EmployeeReviewRepository employeeReviewRepository;

    @Autowired
    private ReviewUpdateBufferService bufferService;
    @Autowired
    private Validator validator;


    public Response processCsv(MultipartFile file) {
        List<EmployeeReview> reviews = new ArrayList<>();
        Map<Integer, List<String >>  violationsMap= new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());
            int row = 1;
            for (CSVRecord csvRecord : csvParser) {

                EmployeeReview review = new EmployeeReview();
                review.setEmployeeId(csvRecord.get("EmployeeID"));
                review.setReviewDate(convertDateToUTC(csvRecord.get("ReviewDate")));
                review.setGoal(csvRecord.get("Goal"));
                review.setAchievement(csvRecord.get("Achievement"));
                review.setRating(Float.parseFloat(csvRecord.get("Rating")));
                review.setFeedback(csvRecord.get("Feedback"));

                List<String> violations = validateEmployeeReview(review);
                if(!violations.isEmpty()){
                    violationsMap.put(row, violations);
                }
                else {
                    reviews.add(review);
                }

            }
            saveAll(reviews);
            if(!violationsMap.isEmpty() && !reviews.isEmpty()){
                return CsvErrorResponse.builder()
                        .message("CSV partially processed with some data violations.")
                        .status("400")
                        .errors(violationsMap)
                        .build();
            } else if(reviews.isEmpty()){
                return CsvErrorResponse.builder()
                        .message("Failed to upload and process the file.")
                        .status("400")
                        .errors(violationsMap)
                        .build();
            }
            return SuccessResponse.builder()
                    .status("200")
                    .message("File uploaded and processed successfully.")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file: " + e.getMessage());
        }

    }

    private void saveAll(List<EmployeeReview> reviews) {
        List<String> employeeIds = reviews.stream()
                .map(EmployeeReview::getEmployeeId)
                .collect(Collectors.toList());

        List<EmployeeReview> allByEmployeeIdIn = employeeReviewRepository.findAllByEmployeeIdIn(employeeIds);

        Map<String, EmployeeReview> existingReviewsMap = allByEmployeeIdIn.stream()
                .collect(Collectors.toMap(EmployeeReview::getEmployeeId, review -> review));

        List<EmployeeReview> newReviews = reviews.stream()
                .filter(review -> !existingReviewsMap.containsKey(review.getEmployeeId()))
                .collect(Collectors.toList());

        employeeReviewRepository.saveAll(newReviews);

        reviews.stream()
                .filter(review -> existingReviewsMap.containsKey(review.getEmployeeId()))
                .forEach(review -> {
                    EmployeeReview existingReview = existingReviewsMap.get(review.getEmployeeId());
                    existingReview.setRating(review.getRating());
                    existingReview.setGoal(review.getGoal());
                    existingReview.setAchievement(review.getAchievement());
                    existingReview.setFeedback(review.getFeedback());
                    existingReview.setReviewDate(review.getReviewDate());
                    bufferService.updateReview(existingReview);
                });

    }

    private ZonedDateTime convertDateToUTC(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        return localDateTime.atZone(ZoneOffset.UTC);
    }

    private List<String> validateEmployeeReview(EmployeeReview dto) {
        Set<ConstraintViolation<EmployeeReview>> violations = validator.validate(dto);
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }
}
