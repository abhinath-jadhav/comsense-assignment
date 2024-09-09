package com.comsense.assignment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;

@Data
@Entity
@ToString
public class EmployeeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "EmployeeID is required and cannot be empty.")
    @Size(min = 5, max = 7, message = "Employee ID length should be between 5-7")
    @Pattern(regexp = "E.*" , message = "Employee ID should start with E")
    private String employeeId;

    @NotNull(message = "ReviewDate is required and cannot be empty.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime reviewDate;

    @NotBlank(message = "Goal is required and cannot be empty.")
    private String goal;

    @NotBlank(message = "Achievement is required and cannot be empty.")
    private String achievement;

    @NotNull(message = "Rating is required.")
    @DecimalMin(value = "1.0", message = "Rating must be between 1.0 and 5.0.")
    @DecimalMax(value = "5.0", message = "Rating must be between 1.0 and 5.0.")
    private Float rating;

    @NotBlank(message = "Feedback is required and cannot be empty.")
    private String feedback;

}
