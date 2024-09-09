package com.comsense.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@Builder
public class ErrorResponse extends Response {
    private String message;
    private String status;
    private List<String> error;
}
